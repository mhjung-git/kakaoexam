package com.kakaopay.exam.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.exam.mapper.SprinklingMapper;

@Service
@Transactional
public class SprinklingService {
	@Autowired
    private SprinklingMapper sprinklingMapper;
    
    public boolean getUserchk(HashMap<String, String> map) throws Exception {		
		boolean access_yn = true;
    	int result = sprinklingMapper.getUserchk(map);
    	
		if (result <= 0 ) {
			access_yn = false;
		}
		
        return access_yn;
    }
    
}
