package com.example.shang.etranslate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shang.etranslate.Activity.QuestionDetailActivity;
import com.example.shang.etranslate.JavaBean.OfficeQuestion;
import com.example.shang.etranslate.JavaBean.Question;
import com.example.shang.etranslate.R;
import com.example.shang.etranslate.Tools.ToolUtils;

import java.util.List;

public class HaHaAdapter extends RecyclerView.Adapter<HaHaAdapter.BaseViewHolder> {
    private Context mContext;
    private List<Question> nlist;
    private List<OfficeQuestion> olist;
    private boolean first = true;

    public HaHaAdapter(Context context, List<Question> list, List<OfficeQuestion> officeList) {
        this.mContext = context;
        this.nlist = list;
        this.olist = officeList;
    }

    public void setFlush(boolean flag){
        first = flag;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = View.inflate(mContext, R.layout.office_item, null);
            return new OfficeViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.question_item, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            return new NormalViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder myViewHolder, final int i) {
        if (myViewHolder instanceof NormalViewHolder) {

            ((NormalViewHolder) myViewHolder).question.setText(nlist.get(i - 1).getQuestion());
            ((NormalViewHolder) myViewHolder).answer.setText(nlist.get(i - 1).getAnswer().get(0));
            if (nlist.get(i - 1).getAnswer().size() > 1) {
                ((NormalViewHolder) myViewHolder).has_more.setVisibility(View.VISIBLE);
                ((NormalViewHolder) myViewHolder).count.setText("查看" + nlist.get(i - 1).getAnswer().size() + "个回答");
                ((NormalViewHolder) myViewHolder).count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                        intent.putExtra("object", nlist.get(i - 1));
                        mContext.startActivity(intent);
                    }
                });
            } else {
                ((NormalViewHolder) myViewHolder).has_more.setVisibility(View.GONE);
            }
        } else if (myViewHolder instanceof OfficeViewHolder) {
            if (first){
                first = false;
                ((OfficeViewHolder) myViewHolder).linearLayout.removeAllViews();
                if (olist.size() == 1) {
                    ((OfficeViewHolder) myViewHolder).linearLayout.addView(ToolUtils.addQuestion(mContext, olist.get(0), 3));
                } else if (olist.size() >= 2) {
                    for (int j = 0; j < 2; j++){
                        ((OfficeViewHolder) myViewHolder).linearLayout.addView(ToolUtils.addQuestion(mContext, olist.get(j), 1));
                    }
                }
            }
            ((OfficeViewHolder) myViewHolder).loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OfficeViewHolder) myViewHolder).loadmore.setVisibility(View.INVISIBLE);
                    for (int j = 2; j < olist.size(); j++){
                        ((OfficeViewHolder) myViewHolder).linearLayout.addView(ToolUtils.addQuestion(mContext, olist.get(j), 1));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nlist.size() + 1;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class NormalViewHolder extends BaseViewHolder {

        private TextView question;
        private TextView answer;
        private LinearLayout has_more;
        private TextView count;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            has_more = itemView.findViewById(R.id.has_more);
            count = itemView.findViewById(R.id.other);
        }
    }

    class OfficeViewHolder extends BaseViewHolder {

        private LinearLayout linearLayout;
        private TextView loadmore;

        public OfficeViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.office_question);
            loadmore = itemView.findViewById(R.id.loadmore);
        }
    }
}
