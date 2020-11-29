package com.kakaopay.exam.controller;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.exam.check.CheckService;
//import com.kakaopay.exam.check.CheckService;
import com.kakaopay.exam.recieve.RecieveService;
import com.kakaopay.exam.service.SprinklingService;
import com.kakaopay.exam.throwmoney.ThrowmoneyService;

@RequestMapping(value = "/v1/api")
@RestController
public class SprinklingController {
	@Autowired
	private SprinklingService sprinklingService;

	@Autowired
	private ThrowmoneyService throwmoneyService;

	@Autowired
	private RecieveService recieveService;
	
	
	@Autowired 
	private CheckService checkService;
	 
	

	public boolean checkIsNumber(String s) {
		return s.matches("[0-9]+");
	}

	public boolean checkIsAlpha(String s) {
		return s.matches("[a-zA-Z]+");
	}

	@GetMapping("hello")
	public String hello() {
		return "할수있다!!";
	}
	
	/*
	 * (공통) MemberChk 기능 : hearder에 X_USER_ID, X_ROOM_ID 조회 
	 * 
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public boolean getUserchk(@RequestParam HttpServletRequest request) throws Exception {
		String user_id = request.getHeader("X-USER-ID");
		String room_id = request.getHeader("X-ROOM-ID");
		boolean access_chk = true;
		
		if (!checkIsNumber(user_id)) {access_chk = false; return access_chk;}
		if (!checkIsAlpha(room_id)) {access_chk = false; return access_chk;}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("x-user-id", user_id);
		map.put("x-room-id", room_id);
		
		
		access_chk = sprinklingService.getUserchk(map);
		
		return access_chk;
	}



	/*
	 * sprinklingMoney 기능 : 뿌리기 input : amount, person output : sp_token
	 * 
	 */
	@RequestMapping(value = "/sprinkling", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject sprinklingMoney(HttpServletRequest request, @RequestParam int amount, int person) throws Exception {
		JSONObject resultMsg = new JSONObject();
		
		/*USER CHECK*/
		boolean Access_yn = getUserchk(request);
		if (Access_yn) {
			Object result = null; //token
			result = throwmoneyService.doSprinklingMoney(amount, person);
			
			resultMsg.put("result", result);
		}
		else {
			resultMsg.put("result", "사용 가능 사용자 아님 [ERROR:9999]");
		}
		
		return resultMsg;
	}

	/*
	 * RecieveMoney 기능 : 줍기 input : String token output : int rev_amt
	 * 
	 */
	@RequestMapping(value = "/recieve", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject RecieveMoney(HttpServletRequest request, @RequestParam String token, String recieve_id) throws Exception {
		JSONObject resultMsg = new JSONObject();
		
		/*USER CHECK*/
		boolean Access_yn = getUserchk(request);
		if (Access_yn) {
			Object result = null; // 받은금액
			
			result = recieveService.doRecieveMoney(token, request.getHeader("X-USER-ID"));
			
			resultMsg.put("result", result);
		} 
		else {
			resultMsg.put("result", "사용 가능 사용자 아님 [ERROR:9999]");
		}
		
		return resultMsg;
	}

	/*
	 * CheckHistory 기능 : 조회 input : output :
	 * 
	 */	
	
	 @RequestMapping(value = "/check", method = RequestMethod.POST)
	 @ResponseBody 
	 public JSONObject CheckHistory(HttpServletRequest request, @RequestParam String token, String req_id) throws Exception { 
		 JSONObject resultMsg = new JSONObject();	//뿌린시각, 뿌린금액, 받기완료된 금액
		 	 
		
		boolean Access_yn = getUserchk(request);
		if (Access_yn) {
			JSONArray resultList = null; // 받기 완료된 정보
		
			resultList = checkService.getCheckHistory(token, request.getHeader("X-USER-ID"));
			
			resultMsg.put("result", resultList);
		} 
		else {
			resultMsg.put("result", "사용 가능 사용자 아님 [ERROR:9999]");
		}
		return resultMsg; 
	 }
	 
}
