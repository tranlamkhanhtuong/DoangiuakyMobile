package uitcourse.j11.nt118.appmusichtcl.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uitcourse.j11.nt118.appmusichtcl.Model.Playlist;
import uitcourse.j11.nt118.appmusichtcl.R;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {


    public PlaylistAdapter(@NonNull Context context, int resource,@NonNull List<Playlist> objects) {

        super(context, resource, objects);
    }

    class ViewHolder{
        TextView txttenplaylist;
        ImageView imagbackground, imgplaylist;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.dong_playlist,null);
            viewHolder = new ViewHolder();
            //Log.d("TUONG1",viewHolder.txttenplaylist.toString());
            viewHolder.txttenplaylist = convertView.findViewById(R.id.textviewtenplaylist);
            //Log.d("TUONG2",viewHolder.imgplaylist.toString());
            viewHolder.imgplaylist = convertView.findViewById(R.id.imageviewplaylist);
            //Log.d("TUONG3",viewHolder.imagbackground.toString());
            viewHolder.imagbackground = convertView.findViewById(R.id.imageviewbackgroundplaylist);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Playlist playlist = getItem(position);
        Picasso.with(getContext()).load(playlist.getHinhPlaylist()).into(viewHolder.imagbackground);
        Picasso.with(getContext()).load(playlist.getIcon()).into(viewHolder.imgplaylist);
        viewHolder.txttenplaylist.setText(playlist.getTen());
        return convertView;
    }
}
