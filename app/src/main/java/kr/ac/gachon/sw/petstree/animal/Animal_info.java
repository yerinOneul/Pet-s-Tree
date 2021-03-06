package kr.ac.gachon.sw.petstree.animal;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.ac.gachon.sw.petstree.MainActivity;
import kr.ac.gachon.sw.petstree.R;

public class Animal_info extends Fragment {
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_animal_info, container, false);
        setInfo();
        return root;
    }


    private void setInfo() {
        TextView sido = (TextView) root.findViewById(R.id.sido);
        TextView sigungu = (TextView) root.findViewById(R.id.sigungu);
        TextView shelter = (TextView) root.findViewById(R.id.shelter);
        TextView kindUp = (TextView)root.findViewById(R.id.kindUp);
        TextView kindDown = (TextView)root.findViewById(R.id.kindDown);
        TextView start = (TextView)root.findViewById(R.id.startDate);
        TextView end = (TextView)root.findViewById(R.id.endDate);
        RecyclerView recycler = (RecyclerView)root.findViewById(R.id.recycler_abandoned_list);
        LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearManager);
        Button btn = (Button) root.findViewById(R.id.btn);
        Animal_url url = new Animal_url();
        String key = getString(R.string.service_key); //api_info.xml??? ????????? service key

        //?????? ??????
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                //20210519 ???????????? ??????
                String year = Integer.toString(yy);
                String month = (mm < 10) ? "0"+Integer.toString(mm + 1) : Integer.toString(mm + 1);
                String day = (dd < 10) ? "0"+Integer.toString(dd) : Integer.toString(dd);

                //????????? ???????????? ????????? ????????? ???????????? ??????
                if (end.getVisibility()==View.INVISIBLE){
                    url.setBgnde(year+month+day);
                    start.setText(year+"-"+month+"-"+day+"  ~");
                    end.setVisibility(View.VISIBLE);
                    end.setText("?????? ???");
                }
                // ????????? ?????? ??? ??????????????? ???????????? ????????? toast ??????
                else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd");
                    try {
                        Date startDate = dateFormat.parse(url.getBgnde());
                        Date endDate = dateFormat.parse(year+month+day);
                        if (startDate.compareTo(endDate) <= 0){
                            url.setEndde(year+month+day);
                            end.setText(year+"-"+month+"-"+day);
                        }
                        else{
                            Toast.makeText(getContext(),"????????? ????????? ????????? ??????????????????.",Toast.LENGTH_LONG).show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        };


        //????????? item?????? scroll ??? ??? ?????? ????????? ??????
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recycler.getLayoutManager());
                int total = layoutManager.getItemCount();
                int last = layoutManager.findLastCompletelyVisibleItemPosition();

                if (last >= total - 1) { //????????? item?????? scroll ???
                    int nor = Integer.parseInt(url.getNumOfRows());
                    int pn = Integer.parseInt(url.getPageNo());
                    int tc = Integer.parseInt(url.getTotalCount());

                    //?????? ???????????? ????????? ?????????????????? ??????
                    if ( (nor*pn) < tc ){
                        url.setPageNo(String.valueOf(++pn));
                        String s_url = url.getUrl();
                        //????????? ???????????? ????????? ?????? ????????? ????????????
                        new Animal_API((Animal_adapter) recycler.getAdapter(),url,s_url,key,5).execute();
                    }
                    else
                        Toast.makeText(getContext(),"????????? ??????????????????.",Toast.LENGTH_LONG).show();

                }
            }
        };

        //?????? ??????
        sido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigungu.setVisibility(View.INVISIBLE);
                sigungu.setText("????????? ???");
                url.setOrg_cd("");
                shelter.setVisibility(View.INVISIBLE);
                shelter.setText("????????? ???");
                url.setCare_reg_no("");
                AlertDialog.Builder search = new AlertDialog.Builder(getContext());
                search.setTitle("????????? ??????????????????");
                //?????? ?????? url
                String sido_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sido?ServiceKey=";
                new Animal_API(sido,url,search,sido_url,key,1).execute();
                search.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(url.getUpr_cd()!="")
                            sigungu.setVisibility(View.VISIBLE);
                    }
                });


            }
        });
        //????????? ??????
        sigungu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelter.setVisibility(View.INVISIBLE);
                shelter.setText("????????? ???");
                url.setCare_reg_no("");
                AlertDialog.Builder search = new AlertDialog.Builder(getContext());
                search.setTitle("???????????? ??????????????????");
                //????????? ?????? url
                String sigungu_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sigungu?upr_cd="+url.getUpr_cd()+"&ServiceKey=";
                new Animal_API(sigungu,url,search,sigungu_url,key,2).execute();
                search.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(url.getOrg_cd()!="")
                            shelter.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        //????????? ??????
        shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder search = new AlertDialog.Builder(getContext());
                search.setTitle("???????????? ??????????????????");
                //????????? ?????? url
                String shelter_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/shelter?upr_cd="+url.getUpr_cd()+"&org_cd="+url.getOrg_cd()+"&ServiceKey=";
                search.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Animal_API(shelter,url,search,shelter_url,key,3).execute();
            }
        });

        //?????? ??????
        kindUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindDown.setVisibility(View.INVISIBLE);
                kindDown.setText("?????? ???");
                url.setUpkind("");
                AlertDialog.Builder search = new AlertDialog.Builder(getContext());
                search.setTitle("????????? ??????????????????");
                final String[] list = new String[] {"??????","???","?????????","??????"};
                final String[] list_code = new String[] {"",getString(R.string.dog_code),getString(R.string.cat_code),getString(R.string.etc_code)};
                kindUp.setText(list[0]);
                url.setUpkind(list_code[0]);
                search.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kindUp.setText(list[i]);
                        url.setUpkind(list_code[i]);
                        Log.e("!!!",url.getUrl());

                    }
                });
                search.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(url.getUpkind()!="")
                            kindDown.setVisibility(View.VISIBLE);
                    }
                });
                search.show();
            }
        });

        //?????? ??????
        kindDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder search = new AlertDialog.Builder(getContext());
                search.setTitle("????????? ??????????????????");
                //?????? ?????? url
                String kind_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/kind?up_kind_cd="+url.getUpkind()+"&ServiceKey=";
                search.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Animal_API(kindDown,url,search,kind_url,key,4).execute();
            }
        });

        //?????? ?????? (??????)
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end.setVisibility(View.INVISIBLE);
                end.setText("");
                url.setEndde("");
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(getContext(),dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
            }
        });

        //?????? ?????? (???)
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(getContext() ,dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
            }
        });


        //?????? ?????? ?????? ???
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????? ????????? ???????????? ?????? ?????? -> pageNo ?????????
                url.setPageNo("1");
                String s_url = url.getUrl();
                Log.e("!!!",s_url);
                Animal_adapter adapter = new Animal_adapter();
                //?????? view ?????? ??? ?????? ?????? ????????? ??????
                adapter.setOnItemClickListener(new Animal_adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Animal_url animal = null;
                        //deep copy -- > ?????? copy?????? url??? animal??? ?????? ???????????? ???????????? ????????? ????????? detail view??? ????????? ?????????
                        //???????????? error ??????
                        try {
                            animal = (Animal_url) url.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        animal.setPageNo(String.valueOf((position/10)+1));
                        Animal_detail animaldetailFragment = Animal_detail.getInstance(animal.getUrl(), position%10);
                        Fragment current = ((MainActivity) getActivity()).getFragment();
                        ((MainActivity) getActivity()).addFragment(animaldetailFragment);
                        ((MainActivity) getActivity()).hideFragment(current);

                    }
                });
                recycler.setAdapter(adapter);
                recycler.addOnScrollListener(onScrollListener);
                new Animal_API(adapter,url,s_url,key,5).execute();
            }
        });

    }

}
