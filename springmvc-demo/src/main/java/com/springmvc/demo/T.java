package com.springmvc.demo;

import com.springmvc.demo.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mengran.gao on 2017/10/17.
 */
public class T {

    public static void main(String[] args) throws IOException {
        /*List<ContractShopInfoVo> list = new ArrayList<>(3);
        ContractShopInfoVo l1 = new ContractShopInfoVo();
        l1.setShopId(11);
        l1.setShopName("aaa");
        ContractShopInfoVo l2 = new ContractShopInfoVo();
        l2.setShopId(22);
        l2.setShopName("bbb");
        ContractShopInfoVo l3 = new ContractShopInfoVo();
        l3.setShopId(33);
        l3.setShopName("ccc");
        list.add(l1);
        list.add(l2);
        list.add(l3);

        Stream<ContractShopInfoVo> contractShopInfoVoStream = list.stream().filter(t -> t.getShopId().equals(1));
        Optional<ContractShopInfoVo> first = contractShopInfoVoStream.findFirst();
        System.out.println(first.isPresent());
        ContractShopInfoVo contractShopInfoVo = first.get();

        System.out.println(contractShopInfoVo.getShopName());*/

        /*URL url = new URL("http://img1.uat1.rs.com/g1/M00/02/0F/wKh8yloC1qaAeAzvAAAkFtI7mU452.xlsx");
        InputStream inputStream = url.openStream();
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);

        File file = FileUtils.toFile(url);

        FileUtils.copyURLToFile(url, file);*/

        List<User> users = new ArrayList<>(5);
        User u1 = new User("yiyi", 10, "yiyi@qq.com", new Date());
        User u2 = new User("erer", 20, "erer@qq.com", new Date());
        User u3 = new User("sasa", 30, "sasa@qq.com", new Date());
        User u4 = new User("sisi", 40, "sisi@qq.com", new Date());
        User u42 = new User("sisi", 40, "sisi@qq.com", new Date());
        User u5 = new User("erer", 50, "erer@qq.com", new Date());
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u42);
        users.add(u5);
        System.out.println(users);
        users.remove(u4);
        System.out.println(users);
    }
}
