package com.study.projects.percursos_van.web.mapper.phone;

import com.study.projects.percursos_van.model.Phone;
import com.study.projects.percursos_van.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneMapper {

    public static Phone toPrincipalPhone(String number, User user){
        return new Phone(number, Phone.PhoneType.PRINCIPAL, user);
    }

    public static String[] getNumbersFromPhonesArr(List<Phone> phones){
        if(phones.isEmpty()){
            return null;
        }
        return phones
                .stream()
                .map(Phone::getNumber)
                .toArray(String[]::new);
    }
}
