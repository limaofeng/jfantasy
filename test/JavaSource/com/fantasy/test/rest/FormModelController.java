package org.jfantasy.test.rest;

import org.jfantasy.framework.spring.mvc.bind.annotation.FormModel;
import org.jfantasy.framework.spring.mvc.util.MapWapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/formmodel")  
public class FormModelController {

    //ok   /formmodel/user?user.username=zhang&user.password=123
    @RequestMapping("/user")
    public UserModel user(@FormModel("user") UserModel user) {
        System.out.println(user);
        return user;
    }
    
    //ok   /formmodel/array1?array[0]=zhang&array[1]=li
    @RequestMapping("/array1")  
    public String[] array1(@FormModel("array") String[] array) {
        System.out.println(Arrays.toString(array));
        return array;
    }
    
    //ok   /formmodel/array2?array[0].username=zhang&array[0].password=123&array[1].username=li
    @RequestMapping("/array2")  
    public UserModel[] array2(@FormModel("array") UserModel[] array) {
        System.out.println(Arrays.toString(array));
        return array;
    }
    
    
  //ok   /formmodel/list1?list[0]=123&list[1]=234
    @RequestMapping("/list1")  
    public String list1(@FormModel("list") List<Integer> list) {
        System.out.println(list);
        return "redirect:/success";        
    }
    
    //ok   /formmodel/list2?list[0].username=zhang&list[1].username=li
    @RequestMapping("/list2")  
    public List<UserModel> list2(@FormModel("list") List<UserModel> list) {
        System.out.println(list);
        return list;
    }
    
    //ok   /formmodel/map1?map['0']=123&map["1"]=234
    @RequestMapping("/map1")  
    public MapWapper<String, Integer> map1(@FormModel("map") MapWapper<String, Integer> map) {
        System.out.println(map);
        return map;
    }
  //ok   /formmodel/map2?map['0'].password=123&map['0'].username=123&map["1"].username=234
    @RequestMapping("/map2")  
    public MapWapper<Integer, UserModel> map2(@FormModel("map") MapWapper<Integer, UserModel> map) {
        System.out.println(map);
        return map;
    }
}
