package com.kakaopay.exam.check;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.exam.mapper.SprinklingMapper;
import com.kakaopay.exam.throwmoney.Crypto;
import com.kakaopay.exam.throwmoney.EncInfo;


@Service
@Transactional
public class CheckService {

	@Autowired
	private SprinklingMapper sprinklingMapper;

	public JSONArray getCheckHistory(String token, String req_id) {	//req_id = action_id
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map_detail = new HashMap<String, Object>();
		String EncToken = "";
		try {
			EncToken = Crypto.Encrypt(EncInfo.PubCINST_DKEY, token);
			map.put("token", EncToken);
		} catch (Exception e) {
			System.out.println(" doRecieveMoney e="+e.toString());
		}
		map.put("token", EncToken);
		map.put("req_id", req_id);
	
		Map<Object, Object> obj_comm = null;
		Map<Object, Object> obj_detail = null;
		
		JSONArray resultJson = new JSONArray();
		
		List<Map<Object, Object>> result_comm = sprinklingMapper.getCheckHistoryComm(map);		//공통정보 (group by)
		System.out.println("result_comm.size() = " + result_comm.size());
	
			for(int i = 0; i < result_comm.size(); i++) {
				obj_comm = result_comm.get(i);
				
				System.out.println("SPRINKLING_DT = "+obj_comm.get("INSERT_DT").toString());
				System.out.println("SPRINKLING_AMT = "+obj_comm.get("TOT_AMT").toString());
				System.out.println("TOT_SPRINKLING_AMT = "+obj_comm.get("TOT_RECV_AMT").toString());
				
				
				JSONObject jsonobj = new JSONObject();
				JSONArray jarry = new JSONArray();
				
				jsonobj.put("SPRINKLING_DT", obj_comm.get("INSERT_DT"));
				jsonobj.put("SPRINKLING_AMT", obj_comm.get("TOT_AMT"));
				jsonobj.put("TOT_SPRINKLING_AMT", obj_comm.get("TOT_RECV_AMT"));
				
				map_detail.put("token", EncToken);
				map_detail.put("req_id", req_id);
				
				/* 받기 시간 추가 */
				SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
				String throw_time = format1.format(obj_comm.get("INSERT_DT"));
				
				//map_detail.put("insert_dt", obj_comm.get("INSERT_DT"));
				map_detail.put("insert_dt", throw_time);
		
				List<Map<Object, Object>> result_detail = sprinklingMapper.getCheckHistoryDetail(map_detail);	//세부정보
				System.out.println("result_detail.size() = " + result_detail.size());
				for(int k=0; k < result_detail.size(); k++) {
					
					obj_detail = result_detail.get(k);
					JSONObject jsonDetail = new JSONObject();
					
					System.out.println("DIVI_AMT = "+obj_detail.get("DIVI_AMT").toString());
					System.out.println("RECIEVE_ID = "+obj_detail.get("RECIEVE_ID").toString());
					
					jsonDetail.put("DIVI_AMT", obj_detail.get("DIVI_AMT"));
					jsonDetail.put("RECIEVE_ID", obj_detail.get("RECIEVE_ID"));
					
					jarry.put(jsonDetail);
				}
			
				jsonobj.put("RECV_INFO", jarry);
				
				resultJson.put(jsonobj);
			}

		return resultJson;
	}
}

