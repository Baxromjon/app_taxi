package ai.ecma.server.service;

import ai.ecma.server.entity.MadeYear;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.MadeYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MadeYearService {
    @Autowired
    MadeYearRepository madeYearRepository;

    public List<MadeYear> getMadeYearService() {
        return madeYearRepository.findAll();
    }

    public String addmadeYearService(MadeYear madeYear) {
        try {
            madeYearRepository.save(madeYear);
            return "Save";
        } catch (Exception e) {
            return "Not Save";
        }
    }

    public MadeYear getMadeYearByIdService(Integer id) {
        try {
            return madeYearRepository.findById(id).get();
        } catch (Exception t) {
            return new MadeYear();
        }
    }

    public String deletMadeYearService(Integer id) {
        try {
            madeYearRepository.deleteById(id);
            return "Delet";
        } catch (Exception y) {
            return "Not Delete";
        }
    }

    public String updateMadeYearService(Integer id, MadeYear madeYear) {
        Optional<MadeYear> optionalMadeYear = madeYearRepository.findById(id);
        if (!optionalMadeYear.isPresent())
            return "Yo'q";

        MadeYear madeYear1 = optionalMadeYear.get();
        madeYear1.setValue(madeYear.getValue());
        madeYearRepository.save(madeYear1);
        return "Tahrirlandi";
    }
}
