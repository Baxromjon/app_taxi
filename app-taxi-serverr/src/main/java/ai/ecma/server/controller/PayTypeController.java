package ai.ecma.server.controller;

import ai.ecma.server.entity.PayType;
import ai.ecma.server.payload.Result;
import ai.ecma.server.service.PayTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/payType")
public class PayTypeController {
//    @Autowired
//    PayTypeService payTypeService;
//
//    @GetMapping
//    public List<PayType> getPayTypeList() {
//        return payTypeService.getPayTypeList();
//    }
//
//    @GetMapping("/{id}")
//    public PayType getPayTypeById(@PathVariable Integer id) {
//        return payTypeService.getPayTypeById(id);
//    }
//
//    @PostMapping
//    public Result addPayType(@RequestBody PayType payType) {
//        return payTypeService.addPayType(payType);
//    }
//
//    @DeleteMapping("/{id}")
//    public Result deletePayTypeById(@PathVariable Integer id) {
//        return payTypeService.deletePayTypeById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Result editPayTypeById(@PathVariable Integer id, @RequestBody PayType payType) {
//        return payTypeService.editPayTypeById(id, payType);
//    }
}
