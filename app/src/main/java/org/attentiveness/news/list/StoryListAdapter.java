package org.attentiveness.news.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.attentiveness.news.R;
import org.attentiveness.news.data.Story;
import org.attentiveness.news.net.GetNews;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onStoryClicked(Story story);
    }

    private List<Story> mStoryList;
    private OnItemClickListener mOnItemClickListener;

    StoryListAdapter() {
        this.mStoryList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Story story = this.mStoryList.get(position);
        List<String> imageUrlList = story.getImageList();
        final String imageUrl = story.getImage();

        final ImageView imageView = holder.mImageView;
        TextView titleView = holder.mTitleView;
        TextView introView = holder.mIntroView;
        titleView.setText(story.getTitle());
        String intro = story.getIntro();
        while(intro.length() > 0 && (intro.charAt(0) == ' ' || intro.charAt(0) == '　'))
            intro = intro.substring(1);
        introView.setText(intro);

        try {
            if(imageUrl.substring(0,7).equals("baidu::")) {
                new Observable<String>() {
                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {
                        try {
                            String iu = GetNews.getINSTANCE().findPicture(imageUrl.substring(7));
                            observer.onNext(iu);
                        } catch (Exception e) {
                            observer.onError(e);
                        } finally {
                            observer.onComplete();
                        }

                    }
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                System.out.println("Subscribe");
                            }

                            @Override
                            public void onNext(String s) {
                                Picasso.with(holder.mImageView.getContext()).load(imageUrl).error(R.mipmap.ic_read).into(imageView);
                                story.setImage(s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("baidu img: " + e);
                            }

                            @Override
                            public void onComplete() {
                                System.out.println("Complete");
                            }
                        });
            }
            else
                Picasso.with(holder.mImageView.getContext()).load(imageUrl).error(R.mipmap.ic_read).into(imageView);
        }catch(Exception e) {
            Picasso.with(holder.mImageView.getContext()).load("MoFeng").error(R.mipmap.ic_search).into(imageView);
            System.out.println(imageUrl + " Img error: " + e);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onStoryClicked(story);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mStoryList.size();
    }

    void setItemList(List<Story> list) {
        if (list == null) {
            throw new IllegalArgumentException("The arguments must not be null.");
        }
        this.mStoryList = list;
        notifyDataSetChanged();
    }

    void addItemList(List<Story> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.mStoryList.addAll(list);
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_img)
        ImageView mImageView;
        @BindView(R.id.tv_title)
        TextView mTitleView;
        @BindView(R.id.tv_intro)
        TextView mIntroView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
