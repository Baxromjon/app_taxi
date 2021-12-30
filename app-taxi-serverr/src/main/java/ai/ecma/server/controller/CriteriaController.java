package ai.ecma.server.controller;

import ai.ecma.server.entity.Criteria;
import ai.ecma.server.payload.Result;
import ai.ecma.server.service.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/criteria")
public class CriteriaController {

//    @Autowired
//    CriteriaService criteriaService;
//
//    @GetMapping
//    public List<Criteria> getCriteriaList(){
//    return criteriaService.getCriteriaByService();
//    }
//    @GetMapping("/{id}")
//    public Criteria getCriteriaByid(@PathVariable Integer id){
//        return criteriaService.getCriteriaById(id);
//    }
//    @PostMapping
//    public Result addCriteria(@RequestBody Criteria criteria) {
//        return criteriaService.addCriteria(criteria);
//    }
//    @PutMapping("/{id}")
//    public Result editCriteriaId(@PathVariable Integer id,@RequestBody Criteria criteria){
//        return criteriaService.editCriteriaByIdService(id,criteria);
//    }
//    @DeleteMapping("/{id}")
//    public Result deleteCriteriaId(@PathVariable Integer id){
//        return criteriaService.deleteCriteriaByIdService(id);
//        }

}
