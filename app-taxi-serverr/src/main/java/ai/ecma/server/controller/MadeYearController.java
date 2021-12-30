package ai.ecma.server.controller;

import ai.ecma.server.entity.MadeYear;
import ai.ecma.server.service.MadeYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/madeYear")
public class MadeYearController {
    @Autowired
    MadeYearService madeYearService;

    @GetMapping
    public List<MadeYear> getMadeYear() {
        return madeYearService.getMadeYearService();
    }

    @PostMapping
    public String addMadeYear(@RequestBody MadeYear madeYear) {
        return madeYearService.addmadeYearService(madeYear);
    }

    @GetMapping("/{id}")
    public MadeYear getMadeYearBuId(@PathVariable Integer id) {
        return madeYearService.getMadeYearByIdService(id);
    }

    @DeleteMapping("/{id}")
    public String deleteMadeYear(@PathVariable Integer id) {
        return madeYearService.deletMadeYearService(id);
    }

    @PutMapping("/{id}")
    public String updateMadeYear(@PathVariable Integer id, @RequestBody MadeYear madeYear) {
        return madeYearService.updateMadeYearService(id, madeYear);
    }
}
