package ys.app.pad.adapter.appointment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.ServiceInfo;


public class ServerSortAdapter extends RecyclerView.Adapter<ServerSortAdapter.MyViewHolder> {
	private final Activity mActivity;
	protected List<ServiceInfo> list=new ArrayList<>();

	public void setListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	private OnItemClickListener listener;


	public ServerSortAdapter(Activity activity) {
		this.mActivity = activity;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	public ServiceInfo getAnimalType(int position){
		return list.get(position);
	}

	public int getCount() {
		return this.list.size();
	}

	/**
	 * 根据分类的首字母的char ascii值获取第一次出现该首字母的位置	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.bind(list.get(position), position,listener);
	}


	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater
				.from(mActivity)
				.inflate(R.layout.item_animal_type_kind, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public int getItemCount() {
		return list == null ? 0 : list.size();
	}



	/**
	 *当ListView数据发生变化时,调用此方法来更新ListView
	 *
	 * @param list
	 */
	public void updateListView(List<ServiceInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}


	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView tvTitle;
		private View itemView;



		public MyViewHolder(final View itemView) {
			super(itemView);
			this.itemView=itemView;

			tvTitle = (TextView) itemView.findViewById(R.id.title);

		}
		public void bind(ServiceInfo model, final int position, final OnItemClickListener listener){
			tvTitle.setText(model.getName());

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (listener!=null){
						listener.onItemClick(view,position);
					}
				}
			});
		}

	}
}