package com.fantasy.mall.goods.web.converter;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.web.converter.FileDetailStoreConverter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.mall.goods.bean.GoodsImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoodsImageStoreConverter extends FileDetailStoreConverter {

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Object fileDetails = super.convertFromString(context, values, toClass);
		if(fileDetails == null){
			return null;
		}
		if(fileDetails instanceof FileDetail){
			return convertToGoodsImage((FileDetail)fileDetails);
		}
		if(fileDetails instanceof FileDetail[]){
			List<GoodsImage> goodsImages = new ArrayList<GoodsImage>();
			for(FileDetail fileDetail : (FileDetail[])fileDetails){
				goodsImages.add(convertToGoodsImage(fileDetail));
			}
			return goodsImages.toArray(new GoodsImage[goodsImages.size()]);
		}
		return fileDetails;
	}

	private GoodsImage convertToGoodsImage(FileDetail fileDetail){
		return BeanUtil.copyProperties(new GoodsImage(),fileDetail);
	}
}
