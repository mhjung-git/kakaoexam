package com.kakaopay.exam.throwmoney;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.exam.mapper.SprinklingMapper;

@Service
@Transactional
public class ThrowmoneyService {	
	
	@Autowired
	private SprinklingMapper sprinklingMapper;
	
	public String doSprinklingMoney(int amount, int person) throws Exception {
		int split_amt = 0;
		int cnt = 0;

		HashMap<String, Object> map = new HashMap<String, Object>();
				
		/* 토큰 만들기  - 3자리 문자열 & 암호화*/
		StringBuffer token_buff = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 3; i++) {
		    int rIndex = rnd.nextInt(3);
		    switch (rIndex) {
		    case 0:
		        // a-z
		    	token_buff.append((char) ((int) (rnd.nextInt(26)) + 97));
		        break;
		    case 1:
		        // A-Z
		    	token_buff.append((char) ((int) (rnd.nextInt(26)) + 65));
		        break;
		    }
		}
		String EncToken = Crypto.Encrypt(EncInfo.PubCINST_DKEY, token_buff.toString());
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String throw_time = format1.format(time);
		
		map.put("token", EncToken);
		map.put("tot_amt", amount);
		map.put("action_id", person);
		map.put("throw_dt", throw_time);
		
		for(int i = 0; i < person; i++) {
			double rnd_amt = 0;
			int i_random = 0;
			
			double target_amt = amount/10;
			int i_target_amt = (int)target_amt;
			
			if (i_target_amt <= 1) {
				/* insert - TT_SPRINKLING */
				map.put("divi_amt", split_amt);
				
				cnt = sprinklingMapper.doSprinklingMoney(map);
				if(cnt != 1){
					throw new Exception("====doSprinklingMoney 실패 : "+ String.valueOf(cnt) +"건 실행됨====");
				}
			} else {
				while(true) {
					rnd_amt = Math.random();
					i_random = (int)rnd_amt;
					
					/* 뿌리기금액 */
					split_amt = i_target_amt * i_random;
					
					if(split_amt >= amount) {
						continue;
					}
					else {
						/* insert - TT_SPRINKLING */
						map.put("divi_amt", split_amt);
						
						cnt = sprinklingMapper.doSprinklingMoney(map);
						if(cnt != 1){
							throw new Exception("====doSprinklingMoney 실패 : "+ String.valueOf(cnt) +"건 실행됨====");
						}
						
						amount -= split_amt;
						break;											
					}
				}	
			}
		}
		
		return Crypto.Decrypt(EncInfo.PubCINST_DKEY, EncToken);
    }

}