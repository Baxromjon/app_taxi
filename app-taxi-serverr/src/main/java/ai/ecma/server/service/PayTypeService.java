package ai.ecma.server.service;

import ai.ecma.server.entity.PayType;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.PayTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayTypeService {

    @Autowired
    PayTypeRepository payTypeRepository;

    public Result addPayType(PayType payType) {
        boolean existsByName = payTypeRepository.existsByName(payType.getName());
        if (existsByName)
            return new Result("Bunday PayType mavjud!!!", false);
        payTypeRepository.save(payType);
        return new Result("Saqlandi", true);
    }

    public List<PayType> getPayTypeList() {
        return payTypeRepository.findAll();
    }

    public PayType getPayTypeById(Integer id) {
        Optional<PayType> optionalPayType = payTypeRepository.findById(id);
        if (!optionalPayType.isPresent())
            return new PayType();
        return optionalPayType.get();
    }

    public Result deletePayTypeById(Integer id) {
        try {
            payTypeRepository.deleteById(id);
            return new Result("O'chirildi", true);
        } catch (Exception e) {
            return new Result("O'chirilmadi", false);
        }
    }

    public Result editPayTypeById(Integer id, PayType payType) {
        Optional<PayType> optionalPayType = payTypeRepository.findById(id);
        if (!optionalPayType.isPresent())
            return new Result("Bunday type mavjud emas", false);
        PayType payType1 = optionalPayType.get();
        payType1.setName(payType.getName());
        payTypeRepository.save(payType1);
        return new Result("Tahrirlandi", true);
    }
}
