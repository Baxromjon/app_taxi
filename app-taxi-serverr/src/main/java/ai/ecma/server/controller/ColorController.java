package ai.ecma.server.controller;

import ai.ecma.server.entity.Color;
import ai.ecma.server.payload.Result;
import ai.ecma.server.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/color")
public class ColorController {


    @Autowired
    ColorService colorService;


    @PostMapping
    public Result addColor(@RequestBody Color color) {
        return colorService.addColor(color);
    }

    @GetMapping
    public List<Color> getAllColors() {
        return colorService.getAllColors();
    }


    @GetMapping("/{id}")
    public Color getColorById(@PathVariable Integer id) {
        return colorService.getColorById(id);
    }


    @DeleteMapping("/{id}")
    public Result deleteColorById(@PathVariable Integer id) {
        return colorService.deleteColorById(id);
    }

    @PutMapping("/{id}")
    public Result editColor(@PathVariable Integer id, @RequestBody Color color) {
        return colorService.editColor(id, color);
    }

}
