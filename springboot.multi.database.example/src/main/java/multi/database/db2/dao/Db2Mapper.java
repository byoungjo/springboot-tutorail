package multi.database.db2.dao;

import multi.database.model.Employ;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import multi.database.model.Model;

public interface Db2Mapper {
	@Select("SELECT * FROM multi_db_model2 WHERE model_id = #{modelId}")
	public Model findModelByModelId(String modelId);

	@Select("SELECT EMP_ID, ENO, EMP_KORN_NM FROM CI0020 WHERE EMP_ID = #{empId}")
	public Employ findEmployId(String empId);

	@Insert("INSERT INTO multi_db_model2 (model_id) VALUES (#{modelId})")
	public int saveModel(Model model);
}

