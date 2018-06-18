package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dmk.busara_androidtask.R;

import java.util.ArrayList;

import model.Video;

public class VideosAdapter extends BaseAdapter {


    private ArrayList<Video> videos;
    private Context context;
    public VideosAdapter(Context context, ArrayList<Video> videos){
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if(view == null) {
            view = View.inflate(context, R.layout.row_category, null);

            viewHolder = new ViewHolder();

            viewHolder.txtDesc = view.findViewById(R.id.txtDesc);
            viewHolder.txtTitle = view.findViewById(R.id.txtTitle);

            view.setTag(viewHolder);

        }else
            viewHolder = (ViewHolder)view.getTag();

        viewHolder.txtTitle.setText(videos.get(i).name);
        viewHolder.txtDesc.setText(videos.get(i).description);


        return view;
    }


    static class ViewHolder{
        TextView txtTitle,txtDesc;
    }
}
