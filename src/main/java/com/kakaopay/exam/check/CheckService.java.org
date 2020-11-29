package com.kakaopay.exam.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.exam.mapper.SprinklingMapper;


@Service
@Transactional
public class CheckService {

	@Autowired
	private SprinklingMapper sprinklingMapper;

	public JSONArray getCheckHistory(String token, String req_id) {	//req_id = action_id
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map_detail = new HashMap<String, Object>();
		
		map.put("token", token);
		map.put("rep_id", req_id);
	
		Map<Object, Object> obj_comm = null;
		Map<Object, Object> obj_detail = null;
		
		JSONArray resultJson = new JSONArray();
		
		List<Map<Object, Object>> result_comm = sprinklingMapper.getCheckHistoryComm(map);		//공통정보 (group by)
		try {
			for(int i = 0; i < result_comm.size(); i++) {
				obj_comm = result_comm.get(i);
				JSONObject jsonobj = new JSONObject();
				JSONArray jarry = new JSONArray();
				
				jsonobj.put("SPRINKLING_DT", obj_comm.get("INSERT_DT"));
				jsonobj.put("SPRINKLING_AMT", obj_comm.get("TOT_AMT"));
				jsonobj.put("SPRINKLING_AMT", obj_comm.get("TOT_RECV_AMT"));
				
				map_detail.put("token", token);
				map_detail.put("rep_id", req_id);
				map_detail.put("insert_dt", obj_comm.get("INSERT_DT"));
				
				List<Map<Object, Object>> result_detail = sprinklingMapper.getCheckHistoryDetail(map_detail);	//세부정보
				for(int k=0; k < result_detail.size(); k++) {
					obj_detail = result_detail.get(i);
					JSONObject jsonDetail = new JSONObject();
					
					
					jsonDetail.put("DIVI_AMT", obj_detail.get("DIVI_AMT"));
					jsonDetail.put("RECIEVE_ID", obj_detail.get("RECIEVE_ID"));
					
					jarry.put(jsonDetail);
				}
				jsonobj.put("RECV_INFO", jarry);
				
				resultJson.put(jsonobj);
			}
				
		} catch (JSONException e) { e.printStackTrace(); }
		 
		return resultJson;
	}
}

