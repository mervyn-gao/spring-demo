package com.springmvc.demo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    }
}
