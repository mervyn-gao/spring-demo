package com.springmvc.demo.test;

import java.io.File;
import java.util.List;

/**
 * @author mengran.gao
 * @date 2018-08-24 10:02
 * @description TODO
 */
public class ExcelTest {

    public static void main(String[] args) {
        File file = new File("D:\\work-project\\支付清结算\\7月需求\\店铺黑名单\\店铺黑名单模板.xlsx");
        RedStarImportExcel<ShopBlacklistDTO> redStarImport = new RedStarImportExcel<>(file, ShopBlacklistDTO.class);
        List<ShopBlacklistDTO> dataList = redStarImport.getDataList();
        System.out.println(dataList);
    }
}
