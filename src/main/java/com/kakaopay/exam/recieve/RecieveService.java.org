package com.kakaopay.exam.recieve;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.exam.mapper.SprinklingMapper;

@Service
@Transactional
public class RecieveService {

	@Autowired
	private SprinklingMapper sprinklingMapper;
	
	public String doRecieveMoney(String token, String recieve_id) {
		String resultMsg = "";
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("token", token);
		map.put("recieve_id", recieve_id);
		
		String recieve = sprinklingMapper.getReiceveChk(map);
		
		if (recieve.equals("0")) {	//update
			HashMap<String, String> chk_map = new HashMap<String, String>();
			chk_map.put("token", token);
			String Able_flag = sprinklingMapper.getAabledMoneyChk(chk_map);
			
			if (Able_flag.equals("0")) { // 10분이내 받기 가능 
				int update_result = sprinklingMapper.doRecieveMoney(map);
				if (update_result==1) {
					String recv_amt = sprinklingMapper.getReiceveAmt(map);
					resultMsg = recv_amt;
				}
				else {
					resultMsg = "받기 오류발생 [ERROR:9013]";
				}
			}
			else {
				resultMsg = "유효 시간이 지난 뿌리기 입니다. [ERROR:9012]";
			}
		}
		else {
			resultMsg = "이미 받은 뿌리기 입니다. [ERROR:9011]";
		}
		return resultMsg;
	}

}
