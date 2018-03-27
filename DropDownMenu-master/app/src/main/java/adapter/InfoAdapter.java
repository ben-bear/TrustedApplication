package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.yyy.djk.dropdownmenu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fragment.IndexFragment;
import untils.ViewHolder;

/**
 * Created by Administrator on 2017/12/10.
 */

public class InfoAdapter extends BaseAdapter {
    private final int TYPE_1 = 0;    //类型1 正确
    private final int TYPE_2 = 1;     //类型2  错误
    private ListView mListView;
    private LayoutInflater mInflater = null;
    List<Integer> error ;
    List<Map<String, Object>> data;
    public InfoAdapter(Context context, List<Map<String, Object>> data,ListView mListView,List<Integer> error) {
        //根据context上下文加载布局，这里的是Demo17Activity本身，即this
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
        this.mListView = mListView;
        this.error  = error;
    }

    @Override
    public int getItemViewType(int position) {
        if (error!=null  && error.contains(data.get(position).get("pos")))
            return TYPE_2;
        else
            return TYPE_1;

    }



    @Override
    public int getCount() {
        //How many items are in the data set represented by this Adapter.
        //在此适配器中所代表的数据集中的条目数
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // Get the data item associated with the specified position in the data set.
        //获取数据集中与指定索引对应的数据项
        return position;
    }

    @Override
    public long getItemId(int position) {
        //Get the row id associated with the specified position in the list.
        //获取在列表中与指定索引对应的行id
        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    //获取一个在数据集中指定索引的视图来显示数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {


                    holder = new ViewHolder();
                    //根据自定义的Item布局加载布局
                    convertView = mInflater.inflate(R.layout.list_item_dish, null);
                    holder.img = (ImageView) convertView.findViewById(R.id.img);
                    holder.title = (TextView) convertView.findViewById(R.id.tv);
                    holder.info = (TextView) convertView.findViewById(R.id.info);
                    //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                    convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //清除掉listview复用效果
        clearSelectState(holder,position);

        holder.img.setImageResource((Integer) data.get(position).get("img"));
        holder.title.setText((String) data.get(position).get("title"));
        holder.info.setText((String) data.get(position).get("info"));
        //把错误数据集合中的数据标红
        if(error!=null && error.contains(data.get(position).get("pos"))){
            holder.title.setTextColor(0xffff00ff);
        }

//            System.out.println("错误列表"+position+data.get(position).get("interger"));
//            holder.title.setTextColor(0xffff00ff);


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateItemView(position);//状态改变时，调用，这里是为了演示方便
//
//            }
//        });

        return convertView;
    }
    @SuppressLint("ResourceAsColor")
    public void clearSelectState(ViewHolder viewHolder, int pos){
            if(!(error!=null && error.contains(data.get(pos).get(pos)))){
                    viewHolder.title.setTextColor(Color.BLACK);
            }

    }

//    public void updateItemView(int position) {
//        //得到第一个可显示控件的位置，
//        int visiblePosition = mListView.getFirstVisiblePosition();
//        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
//        int index = position - visiblePosition;
//        if (index >= 0) {
//            //得到要更新的item的view
//            View view = mListView.getChildAt(index);
//            //从view中取得holder
//            ViewHolder holder = (ViewHolder) view.getTag();
//            //更改状态
//            holder.title.setText("测试数据");
//            //直接更改数据源
//            data.get(index).put("title", "测试数据");
//        }
//    }
private void updateSingleRow(int count,int position){
    //获取屏幕可见的最顶端item的位置，
    int startView=mListView.getFirstVisiblePosition();
    View mView=mListView.getChildAt(position-startView);
    ViewHolder mViewHolder=(ViewHolder) mView.getTag();
    mViewHolder.title.setText(count+"人觉得赞");
    data.get(position).put("title",count+"人觉得赞");
}
}
