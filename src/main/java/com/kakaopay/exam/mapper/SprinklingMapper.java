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
		int result = sqlSession.insert("sprinklingMapper.doSprinklingMoney", params);
		return result;	 
	 }
	 
	
	public int doRecieveMoney(HashMap<String, Object> map) {	//result : 받은 금액
		int result = sqlSession.update("sprinklingMapper.doRecieveMoney", map);
		return result;
	}

	public String getReiceveChk(HashMap<String, Object> map) {	//result : count 숫자 (0:진행 / 1이상:이미 받음)
		String result = sqlSession.selectOne("sprinklingMapper.getReiceveChk", map);
		return result;		
	}
	
	public String getAabledMoneyChk(HashMap<String, String> map) {	//result : count 숫자 (0:받기불가능 / 1이상:받기가능)
		String result = sqlSession.selectOne("sprinklingMapper.getAabledMoneyChk", map);
		return result;		
	}

	public String getReiceveAmt(HashMap<String, Object> map) {
		String recv_amt = sqlSession.selectOne("sprinklingMapper.getRecieveAmt", map);
		return recv_amt;
	}
	

	public List<Map<Object, Object>> getCheckHistoryDetail(HashMap<String, Object> map) {
		List<Map<Object, Object>> list = sqlSession.selectList("sprinklingMapper.getCheckHistoryDetail", map);
		return list;
	}

	public List<Map<Object, Object>> getCheckHistoryComm(HashMap<String, Object> map) {
		List<Map<Object, Object>> list = sqlSession.selectList("sprinklingMapper.getCheckHistoryComm", map);
		return list;
	}	

}

	
