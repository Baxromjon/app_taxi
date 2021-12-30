package ai.ecma.server.service;

import ai.ecma.server.entity.Criteria;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.CriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CriteriaService {
    @Autowired
    CriteriaRepository criteriaRepository;

    public List<Criteria> getCriteriaByService() {
        return criteriaRepository.findAll();
    }

//======================================================
    public Criteria getCriteriaById(Integer id) {
        Optional<Criteria> byId = criteriaRepository.findById(id);
        if (byId.isPresent())
            return byId.get();
        return null;
    }

//=====================================================
    public Result addCriteria(Criteria criteria) {
        try {
            criteriaRepository.save(criteria);
            return new Result("Save!", true);
        } catch (Exception e) {
            return new Result("This criterion exists!", false);
        }
    }

//=====================================================
    public Result editCriteriaByIdService(Integer id, Criteria criteria) {
        try {
            Optional<Criteria> byId = criteriaRepository.findById(id);
            if (!byId.isPresent()) {
                return new Result("Could not edit", false);
            }
            Criteria criteria1 = byId.get();
            criteria1.setName(criteria.getName());
            criteria1.setDescription(criteria.getDescription());
            criteria1.setNegative(criteria.isNegative());
            criteriaRepository.save(criteria1);
            return new Result("Edited", true);
        }catch (Exception e){
            return new Result("Bunday Criteria yo`q",false);
        }
    }

 //=====================================================
    public Result deleteCriteriaByIdService(Integer id) {
        try {
            criteriaRepository.deleteById(id);
            return new Result("Ma`lumot o`chirildi", true);
        } catch (Exception e) {
            return new Result("Id noto`g`ri kiritilgan", false);
        }
    }

}
