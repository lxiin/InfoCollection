package com.hxgd.collection.audio;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxgd.collection.R;
import com.hxgd.collection.db.RecordEntity;

public class AudioAdapter extends BaseQuickAdapter<RecordEntity, BaseViewHolder> {

    public AudioAdapter() {
        super(R.layout.item_audio);
    }


    @Override
    protected void convert(BaseViewHolder helper, RecordEntity item) {
        helper.setText(R.id.tv_title,"名称："+item.itemName);
        helper.setText(R.id.tv_path,"发言人："+item.userName+"       语言类别:"+item.laguageType);
        helper.setImageResource(R.id.iv_type,item.recordType == 1 ? R.drawable.ic_audio : R.drawable.ic_video);
        helper.setText(R.id.tv_create_time,"创建时间："+item.getTimeAdd());
        helper.setImageResource(R.id.iv_upload,item.isHasSync() ? R.drawable.ic_uploaded : R.drawable.ic_upload);
        helper.addOnClickListener(R.id.ll_layout);
        helper.addOnClickListener(R.id.iv_del);
        helper.addOnClickListener(R.id.iv_type);
        helper.addOnClickListener(R.id.iv_upload);
        helper.addOnLongClickListener(R.id.rl_layout);
    }

}
