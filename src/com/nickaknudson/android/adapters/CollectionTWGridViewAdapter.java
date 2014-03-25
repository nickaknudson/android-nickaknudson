package com.nickaknudson.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayAdapterView.OnItemLongClickListener;
import com.jess.ui.TwoWayAdapterView.OnItemSelectedListener;
import com.jess.ui.TwoWayGridView;
import com.nickaknudson.mva.Collection;
import com.nickaknudson.mva.Model;
import com.nickaknudson.mva.adapters.CollectionViewAdapter;

/**
 * @author nick
 *
 * @param <M>
 */
public abstract class CollectionTWGridViewAdapter<M extends Model<M>> extends CollectionViewAdapter<M> {

	/**
	 * @param activity
	 * @param convertView
	 * @param collection
	 */
	public CollectionTWGridViewAdapter(Activity activity, TwoWayGridView convertView, Collection<M> collection) {
		super(activity, convertView, collection);
	}

	/**
	 * @param activity
	 * @param root
	 * @param collection
	 */
	public CollectionTWGridViewAdapter(Activity activity, ViewGroup root, Collection<M> collection) {
		super(activity, root, collection);
	}

	@Override
	protected View generateView(LayoutInflater layoutInflater, ViewGroup root) {
		TwoWayGridView gridView = new TwoWayGridView(getActivity().getBaseContext());
		return gridView;
	}

	@Override
	protected View fillView(View view) {
		TwoWayGridView adapterView = (TwoWayGridView) view;
		fillTWGridView(adapterView);
		adapterView.setAdapter(this);
		adapterView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(TwoWayAdapterView<?> adapterView, View itemView, int position, long id) {
				M model = getItem(position);
				CollectionTWGridViewAdapter.this.onItemClick(adapterView, itemView, model, position, id);
			}
		});
		adapterView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(TwoWayAdapterView<?> adapterView, View itemView, int position, long id) {
				M model = getItem(position);
				return CollectionTWGridViewAdapter.this.onItemLongClick(adapterView, itemView, model, position, id);
			}
		});
		adapterView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(TwoWayAdapterView<?> adapterView, View itemView, int position, long id) {
				M model = getItem(position);
				CollectionTWGridViewAdapter.this.onItemSelected(adapterView, itemView, model, position, id);
			}
			@Override
			public void onNothingSelected(TwoWayAdapterView<?> adapterView) {
				CollectionTWGridViewAdapter.this.onNothingSelected(adapterView);
			}
		});
		return adapterView;
	}

	protected abstract void fillTWGridView(TwoWayGridView adapterView);

	protected abstract void onItemClick(TwoWayAdapterView<?> adapterView, View itemView,
			M model, int position, long id);

	protected abstract boolean onItemLongClick(TwoWayAdapterView<?> adapterView,
			View itemView, M model, int position, long id);

	protected abstract void onItemSelected(TwoWayAdapterView<?> adapterView, View itemView,
			M model, int position, long id);

	protected abstract void onNothingSelected(TwoWayAdapterView<?> adapterView);
}
