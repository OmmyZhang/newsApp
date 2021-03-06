package com.java.group11.list;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.java.group11.data.Story;
import com.java.group11.globalSetting.GlobalSetting;
import com.java.group11.net.GetNews;

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

    private Resources res;

    StoryListAdapter() {
        this.mStoryList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.java.group11.R.layout.item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Story story = this.mStoryList.get(position);
        final String imageUrl = story.getImage();

        final ImageView imageView = holder.mImageView;
        final TextView titleView = holder.mTitleView;
        final TextView introView = holder.mIntroView;
        titleView.setText(story.getTitle());

        String intro = story.getIntro();
        while (intro.length() > 0 && (intro.charAt(0) == ' ' || intro.charAt(0) == '　'))
            intro = intro.substring(1);
        introView.setText(intro);

        if (!GlobalSetting.getINSTANCE().isNoPicture()) {
            Observable<String> ob = new Observable<String>() {
                @Override
                protected void subscribeActual(Observer<? super String> observer) {
                    try {
                        String iu;
                        if (imageUrl.substring(0, 7).equals("baidu::"))
                            iu = GetNews.getINSTANCE().findPicture(imageUrl.substring(7));
                        else
                            iu = imageUrl;
                        observer.onNext(iu);
                    } catch (Exception e) {
                        observer.onError(e);
                    } finally {
                        observer.onComplete();
                    }

                }
            };
            ob.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        private String s2 = "mf", s1 = "mf";

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String s) {
                            if (s.substring(0, 5).equals("baidu")) {
                                s1 = s.split(";")[1];
                                s2 = s.split(";")[2];
                            } else
                                s1 = s;
                            story.setImage(s1);
                            Picasso.with(holder.mImageView.getContext())
                                    .load(s1)
                                    .placeholder(com.java.group11.R.drawable.ic_image_black_24dp)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError() {
                                            if (s2.equals("mf")) {
                                                story.setImage("baidu::" + story.getTitle());
                                                onBindViewHolder(holder, position);
                                            } else {
                                                story.setImage(s2);
                                                Picasso.with(holder.mImageView.getContext())
                                                        .load(s2)
                                                        .placeholder(com.java.group11.R.drawable.ic_image_black_24dp)
                                                        .error(com.java.group11.R.drawable.ic_broken_image_black_24dp)
                                                        .into(imageView);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println("baidu img: " + e);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
        else
            Picasso.with(holder.mImageView.getContext())
                    .load(com.java.group11.R.drawable.ic_image_black_24dp)
                    .error(com.java.group11.R.drawable.ic_image_black_24dp)
                    .into(imageView);

        if (GlobalSetting.getINSTANCE().ifRead(story.getId())) {

            holder.mTitleView.setTextColor(Color.GRAY);
            holder.mIntroView.setTextColor(Color.GRAY);
            holder.mImageView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.LIGHTEN);
        } else {
            holder.mTitleView.setTextColor(res.getColor(com.java.group11.R.color.story_text_color));
            holder.mIntroView.setTextColor(res.getColor(com.java.group11.R.color.story_text_color));
            imageView.clearColorFilter();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mTitleView.setTextColor(Color.GRAY);
                holder.mIntroView.setTextColor(Color.GRAY);
                holder.mImageView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.LIGHTEN);
                GlobalSetting.getINSTANCE().readIt(story.getId());
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

        @BindView(com.java.group11.R.id.iv_img)
        ImageView mImageView;
        @BindView(com.java.group11.R.id.tv_title)
        TextView mTitleView;
        @BindView(com.java.group11.R.id.tv_intro)
        TextView mIntroView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void getResources(Resources src) {
        res = src;
    }

}
