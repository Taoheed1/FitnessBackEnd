package com.qa.persistance.repository;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.qa.persistance.domain.Program;
import com.qa.util.JSONUtil;

@Default
@Transactional(SUPPORTS)
public class ProgramDBRepository implements ProgramRepository {

	@PersistenceContext(unitName = "primary")
	private EntityManager manager;

	@Inject
	private JSONUtil util;

	@Override
	@Transactional(REQUIRED)
	public String getAllPrograms() {
		Query query = manager.createQuery("Select a FROM Program a");
		List<Program> allPrograms = query.getResultList();
		return util.getJSONForObject(allPrograms);
	}

	@Override
	@Transactional(REQUIRED)
	public String getProgramsByType(String programType) {
		Query query = manager.createQuery("Select a FROM Progam a WHERE a.ProgramType = "+ programType);
		List<Program> programsOfType = query.getResultList();
		return util.getJSONForObject(programsOfType);
	}

	@Override
	@Transactional(REQUIRED)
	public String addNewProgram(String program) {
		manager.persist(program);
		return "{\"message\":\"program has been successfully created\"}";
	}

	@Override
	@Transactional(REQUIRED)
	public String deleteProgram(long programID) {
		Program programInDB = findProgram(programID);
		if (programInDB != null) {
			manager.remove(programInDB);
			return "{\"message\":\"program has been successfully deleted\"}";
		}
		 return "{\"message\":\"program does not exist\"}";
	}

	@Override
	@Transactional(REQUIRED)
	public String updateProgram(long programID, String program) {
		Program programInDB = findProgram(programID);
		if (programInDB != null) {
			manager.remove(programInDB);
			manager.persist(program);
			return "{\"message\":\"program has been successfully updated\"}";
		} else {
			return "{\"message\": \"program does not exist\"}";
		}

	}

	private Program findProgram(long programID) {
		return manager.find(Program.class, programID);
	}

}
