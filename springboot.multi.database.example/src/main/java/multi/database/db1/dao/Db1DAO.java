package multi.database.db1.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import multi.database.model.Model;

@Repository("db1DAO")
public class Db1DAO {
	@Autowired
	@Qualifier("db1SqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	public Model findModelByModelId(long modelId) {
		return sqlSession.selectOne("model.findModelByModelId", modelId);
	}

	public int saveModel(Model model) {
		return sqlSession.insert("model.saveModel", model);
	}
}

