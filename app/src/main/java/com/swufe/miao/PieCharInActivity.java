package com.swufe.miao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PieCharInActivity extends AppCompatActivity  implements View.OnClickListener{
    private static final String TAG="PieChartActivity";
    DBBillManager dbBillManager;
    float[] total_income;
    List<Bill> bills;
    String user_id;
    Intent intent;
    int month_current;
    TextView data_month;
    PopupMenu pm;

    private PieChart mChart;
    private ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart_income);
        mChart = findViewById(R.id.pieChart);

        //获得当前用户ID
        SharedPreferences sharedPreferences = getSharedPreferences("myUser", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.i(TAG,"user_id:"+user_id);

        //获取所有账单
        dbBillManager = new DBBillManager(PieCharInActivity.this);
        bills=dbBillManager.listAll();

        //默认选择当前月份
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        month_current=Integer.parseInt(curDateStr.substring(5,7));
        Log.i(TAG,"初始month:"+month_current);
        setData(month_current);

        //下拉菜单
        data_month= findViewById(R.id.data_month);
        initMonthMenu(data_month);
        data_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm.show();
            }
        });
    }

    public void onClick(View btn) {
        if(btn.getId()==R.id.btn_income){
            intent = new Intent(this, PieCharOutActivity .class);
            startActivity(intent);
        }
    }
    //获得详细账单展示
    public float[] inDetail(int month){
        total_income= new float[]{0, 0, 0, 0, 0, 0, 0};
        for(Bill bill:bills){
            if(user_id.equals(bill.getUser_id()) && month==bill.getMonth()){
                for(int i=1;i<=6;i++){
                    if(i==bill.getCategory_id()){
                        total_income[i]+=bill.getCost();
                    }
                }
            }
        }
        return total_income;
    }
    //月份选择 下拉菜单
    private void initMonthMenu(TextView data_month){
        pm=new PopupMenu(PieCharInActivity.this,data_month);
        Menu menu=pm.getMenu();
        pm.getMenuInflater().inflate(R.menu.month,menu);
        // 添加单击数据项事件
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem mitem) {
                // 选择某一个分类后将控件的值改为数据项的内容（catagory是EditText控件）
                data_month.setText(mitem.getTitle());
                month_current=Integer.parseInt(mitem.getTitle().toString());
                //相关修改
                setData(month_current);
                Log.i("run","month "+month_current);
                return true;
            }
        });
    }
    private void setData(int month){
        String[] a2={"","生活费","奖学金","工资","理财","兼职","其他"};
        entries.clear();

        for(int i=1;i<=6;i++){
            entries.add(new PieEntry(inDetail(month)[i], a2[i]));
        }

        mChart.setUsePercentValues(true); //设置是否显示数据实体(百分比，true:以下属性才有意义)
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 5, 5, 5);//饼形图上下左右边距

        mChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]

        //mChart.setCenterTextTypeface(mTfLight);//设置所有DataSet内数据实体（百分比）的文本字体样式
        mChart.setCenterText("饼状图");//设置PieChart内部圆文字的内容

        mChart.setDrawHoleEnabled(true);//是否显示PieChart内部圆环(true:下面属性才有意义)
        mChart.setHoleColor(Color.WHITE);//设置PieChart内部圆的颜色

        mChart.setTransparentCircleColor(Color.WHITE);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        mChart.setTransparentCircleAlpha(110);//设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
        mChart.setHoleRadius(28f);//设置PieChart内部圆的半径(这里设置28.0f)
        mChart.setTransparentCircleRadius(31f);//设置PieChart内部透明圆的半径(这里设置31.0f)

        mChart.setDrawCenterText(true);//是否绘制PieChart内部中心文本（true：下面属性才有意义）

        mChart.setRotationAngle(0);//设置pieChart图表起始角度
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);//设置pieChart图表是否可以手动旋转
        mChart.setHighlightPerTapEnabled(true);//设置piecahrt图表点击Item高亮是否可用

        mChart.animateY(1400, Easing.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        // 获取pieCahrt图列
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f); //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(0f); //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(0f);//设置比例块Y轴偏移量

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);//设置pieChart图表文本字体颜色
//        mChart.setEntryLabelTypeface(mTfRegular);//设置pieChart图表文本字体样式
        mChart.setEntryLabelTextSize(12f);//设置pieChart图表文本字体大小

        PieDataSet dataSet = new PieDataSet(entries, "数据说明");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
}