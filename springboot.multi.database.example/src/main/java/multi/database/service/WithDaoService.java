package multi.database.service;

import lombok.extern.slf4j.Slf4j;
import multi.database.model.Employ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import multi.database.db1.dao.Db1DAO;
import multi.database.db2.dao.Db2DAO;
import multi.database.model.Model;
@Slf4j
@Service("withDaoService")
public class WithDaoService {

	@Autowired
	@Qualifier("db1DAO")
	private Db1DAO db1DAO;
	
	@Autowired
	@Qualifier("db2DAO")
	private Db2DAO db2DAO;

	public Employ moveDb1toDb2(long modelId) {
		log.info(">>>>>>  withDaoService");
		Model model = db1DAO.findModelByModelId(modelId);
		log.info("getFirstName {}", model.getFirstName());
		Employ employ = db2DAO.findEmployId("S0000755");
		log.info("getEmpKornNm {}", employ.getEmpKornNm());
//		return db2DAO.saveModel2(model);
		return employ;
	}
}
