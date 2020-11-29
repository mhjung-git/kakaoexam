package com.kakaopay.exam.recieve;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.exam.mapper.SprinklingMapper;
import com.kakaopay.exam.throwmoney.Crypto;
import com.kakaopay.exam.throwmoney.EncInfo;

@Service
@Transactional
public class RecieveService {

	@Autowired
	private SprinklingMapper sprinklingMapper;
	
	public String doRecieveMoney(String token, String recieve_id) {
		String resultMsg = "";
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		/* 입력받은 토큰값 ENCRYPT */
		String EncToken = "";
		try {
			EncToken = Crypto.Encrypt(EncInfo.PubCINST_DKEY, token);
			map.put("token", EncToken);
		} catch (Exception e) {
			System.out.println(" doRecieveMoney e="+e.toString());
		}
		
		map.put("recieve_id", recieve_id);
		
		System.out.println("map1="+map.toString());
		/* 받기가능여부 체크 - 받을 수 있는 건이 있는지 (0이면 가능) */
		String recieve = sprinklingMapper.getReiceveChk(map);
		System.out.println("recieve="+recieve);
		
		if (recieve.equals("0")) {	//update
			HashMap<String, String> chk_map = new HashMap<String, String>();
			chk_map.put("token", EncToken);
			chk_map.put("recieve_id", recieve_id);
			
			/* 받기가능여부 체크2 - 자신이 뿌리지 않은 건이면서 할당 가능 시간 체크 ()*/
			String Able_flag = sprinklingMapper.getAabledMoneyChk(chk_map);
			System.out.println("Able_flag = "+ Able_flag);
						
			if (!Able_flag.equals("0")) { // 10분이내 받기 가능 
				HashMap<String, Object> update_map = new HashMap<String, Object>();
				update_map.put("token", EncToken);
				update_map.put("recieve_id", recieve_id);
				
				/* 받기 시간 추가 */
				SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
				Date time = new Date();
				String throw_time = format1.format(time);
				
				update_map.put("recieve_dt", throw_time);
								
				
				int update_result = sprinklingMapper.doRecieveMoney(update_map);
				System.out.println("update_result = "+ update_result);
				if (update_result==1) {
					
					/* UPDATE 성공 시 금액 조회 */
					String recv_amt = sprinklingMapper.getReiceveAmt(map);
					resultMsg = recv_amt;
					System.out.println("resultMsg = " + resultMsg);
				}
				else {
					resultMsg = "받기오류(UPDATE) [ERROR:9013]";
				}
			}
			else {
				resultMsg = "자신이 뿌린 건 이거나 유효 시간이 지난 뿌리기 입니다. [ERROR:9012]";
			}
		}
		else {
			resultMsg = "이미 받은 뿌리기 입니다. [ERROR:9011]";
			System.out.println("resultMsg="+resultMsg);
		}
		return resultMsg;
	}

}
