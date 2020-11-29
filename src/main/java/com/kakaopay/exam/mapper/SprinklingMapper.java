package com.kakaopay.exam.mapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("sprinklingMapper")
public class SprinklingMapper {

    @Autowired
    private SqlSession sqlSession;
 
    public void setSqlSession(SqlSession sqlSession){
    	this.sqlSession = sqlSession;
	}	
    
	public int getUserchk(Map<String, String> map) throws Exception {
		int result = sqlSession.selectOne("SprinklingMapper.getUserchk", map);
		return result;
	}

	
	 public int doSprinklingMoney(Map<String, Object> params) {
		 System.out.println("SprinklingMapper map="+params.toString());
		 int result = 0;
		 try {
			 result = sqlSession.insert("SprinklingMapper.doSprinklingMoney", params);
		
		 }catch( Exception e) {
			 System.out.println(" SprinklingMapper e="+e.toString());
		 }
		 
		return result;	 
	 }
	 
	
	public int doRecieveMoney(HashMap<String, Object> map) {	//result : 받은 금액
		System.out.println("doRecieveMoney map="+map.toString());
		int result = 0;
		 try {
			 result = sqlSession.update("SprinklingMapper.doRecieveMoney", map);
		
		 }catch( Exception e) {
			 System.out.println(" SprinklingMapper e="+e.toString());
		 }
		return result;
	}

	public String getReiceveChk(HashMap<String, Object> map) {	//result : count 숫자 (0:진행 / 1이상:이미 받음)
		System.out.println("getReiceveChk map="+map.toString());
		String result = Integer.toString(sqlSession.selectOne("SprinklingMapper.getReiceveChk", map));
		return result;		
	}
	
	public String getAabledMoneyChk(HashMap<String, String> map) {	//result : count 숫자 (0:받기불가능 / 1이상:받기가능)
		System.out.println("getAabledMoneyChk map="+map.toString());
		String result = Integer.toString(sqlSession.selectOne("SprinklingMapper.getAabledMoneyChk", map));
		return result;		
	}

	public String getReiceveAmt(HashMap<String, Object> map) {
		System.out.println("getReiceveAmt map="+map.toString());
		String recv_amt = sqlSession.selectOne("SprinklingMapper.getRecieveAmt", map);
		return recv_amt;
	}
	

	public List<Map<Object, Object>> getCheckHistoryDetail(HashMap<String, Object> map) {
		
		System.out.println("getCheckHistoryDetail map="+map.toString());
		List<Map<Object, Object>> list = sqlSession.selectList("SprinklingMapper.getCheckHistoryDetail", map);
		return list;
	}

	public List<Map<Object, Object>> getCheckHistoryComm(HashMap<String, Object> map) {
	
		System.out.println("getCheckHistoryComm map="+map.toString());
		List<Map<Object, Object>> list = sqlSession.selectList("SprinklingMapper.getCheckHistoryComm", map);
		return list;
	}	

}

	
