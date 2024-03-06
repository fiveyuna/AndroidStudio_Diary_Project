package ddwu.mobile.finalproject.ma01_20190978;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RoutineCursorAdapter extends CursorAdapter {

    private static final String TAG = "CursorAdapter";

    LayoutInflater inflater;
    int layout;

    public RoutineCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.tvRoutineTime == null) { // 항목 찾아주는 작업
            holder.tvRoutineTime = view.findViewById(R.id.tvRoutineTime);
            holder.tvRoutineDid = view.findViewById(R.id.tvRoutineDid);
        }

        // 결합
        holder.tvRoutineTime.setText(cursor.getString(cursor.getColumnIndex(RoutineDBHelper.COL_TIME)));
        holder.tvRoutineDid.setText(cursor.getString(cursor.getColumnIndex(RoutineDBHelper.COL_DID)));
    }

    static class ViewHolder {

        public ViewHolder() { // 처음 일 땐 널
            tvRoutineTime = null;
            tvRoutineDid = null;
        }

        TextView tvRoutineTime;
        TextView tvRoutineDid;
    }



}
