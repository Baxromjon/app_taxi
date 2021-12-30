package ai.ecma.server.service;

import ai.ecma.server.entity.Model;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.payload.ModelDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.BrandRepository;
import ai.ecma.server.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {

    @Autowired
    ModelRepository modelRepository;
    @Autowired
    BrandRepository brandRepository;


    public List<Model> getModelList() {
        return modelRepository.findAll();
    }

    public Result addModel(ModelDto modelDto) {
        boolean existsByName = modelRepository.existsByName(modelDto.getName());
        if (existsByName)
            return new Result("Bunday Model mavjud!!!", false);
        Model model = new Model();
        model.setName(modelDto.getName());
        model.setDescription(modelDto.getDescription());
        model.setBrand(brandRepository.findById(modelDto.getBrandId()).orElseThrow(() -> new ResourceNotFoundException("brand", "id", modelDto.getBrandId())));
        modelRepository.save(model);
        return new Result("Saqlandi", true);
    }

    public Model getModelById(Integer id) {
        Optional<Model> optional = modelRepository.findById(id);
        if (!optional.isPresent()) {
            return new Model();
        }
        return optional.get();
    }


    public Result deleteModelById(Integer id) {
        try {
            modelRepository.deleteById(id);
            return new Result("O'chirildi", true);
        } catch (Exception e) {
            return new Result("O'chirilmadi", false);
        }
    }

    public Result editModel(Integer id, Model model) {
        Optional<Model> optionalModel = modelRepository.findById(id);
        if (!optionalModel.isPresent())
            return new Result("Bunday model mavjud emas", false);
        Model bazadanKeganModel = optionalModel.get();
        bazadanKeganModel.setName(model.getName());
        bazadanKeganModel.setBrand(model.getBrand());
        bazadanKeganModel.setDescription(model.getDescription());
        modelRepository.save(bazadanKeganModel);
        return new Result("Saqlandi", true);
    }
}



