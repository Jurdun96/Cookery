package eatec.cookery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class cardAdapter extends PagerAdapter {
    private List<step> steps;
    private LayoutInflater layoutInflater;
    private Context context;

    public cardAdapter(List<step> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_card,container,false);

        TextView stepNo, shortDescription;

        stepNo = view.findViewById(R.id.cardStepNumber);
        shortDescription = view.findViewById(R.id.cardShortDescritpionText);

        //imageView.setImageResource(steps.get(position).getStepImage());
        shortDescription.setText(steps.get(position).getStepDescription());
        int stepPostion = position + 1;
        stepNo.setText("Step " + stepPostion);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
