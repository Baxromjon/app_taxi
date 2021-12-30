package ai.ecma.server.service;

import ai.ecma.server.entity.Brand;
import ai.ecma.server.payload.BrandDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
//Hello guys. My name is Maruf. Nice to meet you :)
    @Autowired
    BrandRepository brandRepository;

    public Result addBrand(Brand brand) {

        boolean existsByName = brandRepository.existsByName(brand.getName());
        if (existsByName)
            return new Result("Bunday nomlik brand bor!!!", false);


        brandRepository.save(brand);
        return new Result("Saqlandi", true);
    }

    public List <Brand> getlistBrand(){
        List < Brand > brandRepositoryAll = brandRepository.findAll();
        return brandRepositoryAll;
    }


    public Result updateBrand(Integer id, BrandDto brandDto) {
        Optional < Brand > brandOptional = brandRepository.findById(id);
        if (!brandOptional.isPresent())
            return new Result("Bunaqa id topilmadi",false);
        Brand brand = brandOptional.get();
        brand.setName(brandDto.getName());
        brand.setDescription(brandDto.getDescription());
        brandRepository.save(brand);
       return new Result("Saqlandi",true);
    }

    public Result deleteBrand(Integer id) {
        try {
            brandRepository.deleteById(id);
            return new Result("Uchrildi",true);
        } catch (Exception e) {
           return new Result("uchrilmadi",false);
        }


    }

    public Brand getBrandById(Integer id) {
        Optional < Brand > optional = brandRepository.findById(id);
        if (!optional.isPresent()){
            return new Brand();
        }
        return optional.get();
    }
}