package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import br.com.appgo.appgo.R;
import br.com.appgo.appgo.model.TutorialText;

public class TutorialAdapter extends PagerAdapter {
    List<TutorialText> textTutorial;
    Context context;
    LayoutInflater inflater;
    public TutorialAdapter(List<TutorialText> textTutorial, Context context) {
        this.textTutorial = textTutorial;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.tutorial_item_pager, container, false);
        TextView title = view.findViewById(R.id.title_tutorial);
        TextView text = view.findViewById(R.id.text_tutorial);
        title.setText(textTutorial.get(position).getTitleTutorial());
        text.setText(textTutorial.get(position).getTextTutorial());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return textTutorial.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
