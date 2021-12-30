package ai.ecma.server.service;

import ai.ecma.server.entity.Color;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.ColorRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    ColorRepository colorRepository;

    public Result addColor(Color color) {

        boolean existsByName = colorRepository.existsByName(color.getName());
        if (existsByName)
            return new Result("Bunday nomlik rang bor!!!", false);

        boolean existsByCode = colorRepository.existsByCode(color.getCode());
        if (existsByCode)
            return new Result("Bunday codlik rang bor!!!", false);
        colorRepository.save(color);
        return new Result("Saqlandi", true);


    }

    public List<Color> getAllColors() {
        return colorRepository.findAll();

    }

    public Color getColorById(Integer id) {

//        colorRepository.existsById(id);
        Optional<Color> optionalColor = colorRepository.findById(id);
        if (!optionalColor.isPresent())
            return new Color();

        return optionalColor.get();
    }

    public Result deleteColorById(Integer id) {

        colorRepository.deleteById(id);
        return new Result("O'chirildi",true);
    }

    public Result editColor(Integer id, Color color){
        Optional<Color> optionalColor = colorRepository.findById(id);
        if (!optionalColor.isPresent())
            return new Result("Bunday Color mavjud emas", false);

        Color bazadanKeganColor = optionalColor.get();
        bazadanKeganColor.setName(color.getName());
        bazadanKeganColor.setCode(color.getCode());
        bazadanKeganColor.setDescription(color.getDescription());
        colorRepository.save(bazadanKeganColor);
        return new Result("Color o'zgardi", true);
    }
}
