package springBootPickers.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import springBootPickers.domain.OrdersListDTO;
import springBootPickers.domain.PaymentDTO;
import springBootPickers.domain.OrdersDTO;

@Repository
public class OrderRepository {
	@Autowired
	SqlSession sqlSession;
	String namespace = "orderRepositorySql";
	String statement;
	
	public String selectOrderNum() {
		statement = namespace + ".selectOrderNum";
		return sqlSession.selectOne(statement);
	}
	public int orderInsert(OrdersDTO dto) {
		statement = namespace + ".orderInsert";
		return sqlSession.insert(statement, dto);
	}
	public int orderListInsert(Map<String, Object> map) {
		statement = namespace + ".orderListInsert";
		return sqlSession.insert(statement, map);
	}
	public List<OrdersListDTO> orderList(Map<String, String> map) {
		statement = namespace + ".orderList";
		return sqlSession.selectList(statement, map);
	}
	public OrdersDTO orderSelectOne(String orderNum) {
		statement = namespace + ".orderSelectOne";
		return sqlSession.selectOne(statement, orderNum);
	}
	public int paymentInsert(PaymentDTO dto) {
		statement = namespace + ".paymentInsert";
		return sqlSession.insert(statement, dto);
	}
	public int paymentDel(String orderNum) {
		statement = namespace + ".paymentDel";
		return sqlSession.delete(statement, orderNum);
	}
	public int updatePayStatus(String orderNum) {
        String statement = namespace + ".updatePayStatus";
        return sqlSession.update(statement, orderNum);
    }
	public int resetPayStatus(String orderNum) {
        String statement = namespace + ".resetPayStatus";
        return sqlSession.update(statement, orderNum);
    }

	public List<OrdersListDTO> paidOrderList(String memNum) {
	    String statement = namespace + ".paidOrderList";
	    return sqlSession.selectList(statement, memNum);
	}

	public PaymentDTO getPayment(String orderNum) {
	    statement = namespace + ".getPayment";
	    return sqlSession.selectOne(statement, orderNum);
	}

}
