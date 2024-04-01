package com.nezip.bizcard1;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {
	private Bitmap[] mBitmap;
	private ArrayList<Bitmap> mDrawables;
	private ArrayList<String> mImagePaths;
    private Context mContext;
    private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
    private ArrayList<ImageView> mImageViews;
	

    public ImageAdapter(Context c, ArrayList<Bitmap> drawables, ArrayList<String> urls) {
		mContext = c;
		mDrawables = drawables;
		mImagePaths = urls;
		mBitmap = new Bitmap[drawables.size()];
		mImageViews = new ArrayList<ImageView>(drawables.size()); 
    }
    
    public void setImageView(int position, Bitmap bm) {
    	mImageViews.get(position).setImageBitmap(bm);;
    }
    
    public void set(int position, Bitmap bm) {
    	mBitmap[position] = bm;
    }
    
    public Bitmap get(int position) {
    	return mBitmap[position];
    }


    @Override
    public int getCount() {
    	return mDrawables.size();
    }

    @Override
    public Object getItem(int position) {
    	return mDrawables.get(position);
    }

    @Override
    public long getItemId(int position) {
    	return position;
    }

    public void recycle() {
    	RecycleUtils.recursiveRecycle(mRecycleList);
    	mImagePaths = null;
    	
    	for (int i = 0; i < mBitmap.length; i++) {
    		if(mBitmap[i] != null) {
    			if(!mBitmap[i].isRecycled())mBitmap[i].recycle();
    			mBitmap[i]=null;
    		}
    	}
    	
    	for (int i = 0; i < mImageViews.size(); i++) {
    		if(mImageViews.get(i) != null) {
    			if(mImageViews.get(i).getDrawingCache() != null){
    				Bitmap t = mImageViews.get(i).getDrawingCache();
    				mImageViews.get(i).setImageBitmap(null);
    				mImageViews.remove(i);
    				if(!t.isRecycled())t.recycle();
    				t=null;
    			}
    		}
    	}
    }

    public void recycleHalf() {
    	int halfSize = mRecycleList.size() / 2;
    	List<WeakReference<View>> recycleHalfList = mRecycleList.subList(0,	halfSize);
    	
    	for (WeakReference<View> ref : recycleHalfList) {
			recursiveRecycle(ref.get());
		}
    	
    	for (int i = 0; i < halfSize; i++)
    		mRecycleList.remove(0);
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		//recursiveRecycle(convertView);
			//recursiveRecycle(parent);
    		
    		
    		if(mBitmap != null && mBitmap.length > position-2){
	    		for(int k=0; k < position-2; k++){
	    			if(mBitmap[k] != null){
	    				//mImageViews.get(k).setImageBitmap(null);
	    				if(!mBitmap[k].isRecycled())mBitmap[k].recycle();
						mBitmap[k] = null;
					}
				}
    		}
    		
    		if(mBitmap != null && mBitmap.length > position+2){
	    		for(int k=position+2; k < mImageViews.size(); k++){
	    			if(mBitmap[k] != null){
	    				//mImageViews.get(k).setImageBitmap(null);
	    				if(!mBitmap[k].isRecycled())mBitmap[k].recycle();
						mBitmap[k] = null;
					}
				}
    		}
    		
    		
			System.gc();
    		
				
			ImageView i = new ImageView(mContext);   
			if(position >= mImageViews.size()){
    			mImageViews.add(i);
    		}
    		if (convertView == null) {
				try {
					
					if(mBitmap[position] == null) {
						mBitmap[position] = Info.GetImageFromURL(mImagePaths.get(position), mContext);						
					}		
					i.setImageBitmap(mBitmap[position]);	
		
					
					
				} catch (OutOfMemoryError e) {
					
					
				    if (mRecycleList.size() <= parent.getChildCount()) {
				    	throw e;
				    }
				    //Log.w(this + "", e.toString());
				    recycleHalf();
				    System.gc();
				    return getView(position, convertView, parent);
				}catch(Exception e){					
				    //Log.w(this + "", e.toString());
				    
				}
			
				i.setAdjustViewBounds(true);
				i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				
				mRecycleList.add(new WeakReference<View>(i));
				
	    	} else {
	    		i = (ImageView) convertView;
	    		if(mBitmap[position].isRecycled() || mBitmap[position] == null) {
					mBitmap[position] = Info.GetImageFromURL(mImagePaths.get(position), mContext);						
				}		
				i.setImageBitmap(mBitmap[position]);
	        }
    		
    		
    		return i;
	    }
    	
    	public static void recursiveRecycle(View root) {
            if (root == null)
                return;
            
            root.setBackgroundDrawable(null);

            if (root instanceof ViewGroup) {
                ViewGroup group = (ViewGroup)root;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    recursiveRecycle(group.getChildAt(i));
                }

                if (!(root instanceof AdapterView)) {
                    group.removeAllViews();
                }
                
                //Log.d("ViewGroup", root.toString());

            }
            
            if (root instanceof ImageView) {
                ((ImageView)root).setImageBitmap(null);
                //Log.d("ImageView", root.toString());
            }

            root = null;

            return;
        }
    	
    	
    	
    	
    	
    	
	}


