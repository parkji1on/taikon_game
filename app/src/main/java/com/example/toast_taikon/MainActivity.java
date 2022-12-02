package com.example.toast_taikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView txtGredient, txtMenu;
    ImageButton btnRecipe, btnStart, btnRestart;
    ImageButton [] ingreButtons = new ImageButton[16];
    ProgressBar progFood;
    Integer[] ingreBtnIDs = {R.id.ingre0, R.id.ingre1, R.id.ingre2, R.id.ingre3,
                            R.id.ingre4, R.id.ingre5, R.id.ingre6, R.id.ingre7,
                            R.id.ingre8, R.id.ingre9,R.id.ingre10, R.id.ingre11,
                            R.id.ingre12, R.id.ingre13, R.id.ingre14, R.id.ingre15};

    int i;      //반복문을 위한 것
    String ingre = "\n\n\n";   //TextView에 보여줄 텍스트 생성
    String choiceMenu; //선택한 메뉴의 이름
    int[] ingredientList = new int[16]; //선택한 재료가 들어 있는지 여부
    //메뉴의 리스트 (menu) - (choicemenu와 ingredientList를 저장함) - 홀에서 필요한 정보
    Menu menu;  //Menu 객체를 위한것
    //레시피의 확인 절차를 위한 recipebook
    HashMap<String,int[]> recipebook = new HashMap<String,int[]>(){{
        put("햄 치즈 스페셜", new int[]{1,0,1,1,1,1,0,0,0,0,0,0,1,0,0,0});
        put("베이컨 베스트", new int[]{1,0,1,0,1,1,1,0,0,0,0,0,1,0,0,0});
        put("햄 치즈 포테이토", new int[]{1,0,1,0,1,0,0,1,0,0,0,0,0,0,0,1});
        put("더블 소세지", new int[]{1,0,1,0,1,1,0,0,1,0,0,0,0,0,0,0});
        put("새우", new int[]{1,0,1,0,1,1,0,0,0,0,1,0,1,0,0,0});
        put("그릴드 불고기", new int[]{1,0,1,0,1,1,0,0,0,0,0,1,1,0,0,0});
        put("베이컨 치즈 베이글", new int[]{0,1,1,0,1,0,1,0,0,0,0,0,0,0,0,0});
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Toast_taikon");

        progFood = (ProgressBar) findViewById(R.id.progFood);
        txtMenu = (TextView) findViewById(R.id.txtMenu);
        txtGredient = (TextView) findViewById(R.id.txtGredient);
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnRestart = (ImageButton) findViewById(R.id.btnRestart);
        btnRecipe = (ImageButton) findViewById(R.id.btnRecipe);
        for(i=0; i<ingreBtnIDs.length; i++){
            ingreButtons[i] = (ImageButton) findViewById(ingreBtnIDs[i]);
        }

        //menu등록
        registerForContextMenu(btnRecipe);

        //재료버튼이 눌렸을 때
        for(i=0; i<ingreBtnIDs.length; i++){
            final int index;
            index = i;

            ingreButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(index){
                        case 0: ingre = "빵"; break;
                        case 1: ingre = "베이글"; break;
                        case 2: ingre = "계란"; break;
                        case 3: ingre = "햄"; break;
                        case 4: ingre = "치즈"; break;
                        case 5: ingre = "양상추"; break;
                        case 6: ingre = "베이컨"; break;
                        case 7: ingre = "해시브라운"; break;
                        case 8: ingre = "소세지"; break;
                        case 9: ingre = "마카로니"; break;
                        case 10: ingre = "새우"; break;
                        case 11: ingre = "불고기"; break;
                        case 12: ingre = "피클"; break;
                        case 13: ingre = "케챱"; break;
                        case 14: ingre = "머스타드"; break;
                        case 15: ingre = "치즈소스"; break;
                    }
                    //ingre = txtGredient.getText().toString() + "  " +ingreButtons[index].getText().toString();
                    ingre = txtGredient.getText().toString() + "  " + ingre;
                    txtGredient.setText(ingre);
                    //i를 쓰면 안되고 index를 써야함 왜인지는 잘 모르겠다...
                    ingredientList[index]++;    //해당 재료의 갯수 1 증가(여러번 눌러도 되게 만들었음)
                }
            });
        }

        //조리시작 버튼을 눌렀을 때
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //음식의 이름과 재료로 점수를 계산(scoring)
                menu = new Menu(choiceMenu, scoring(choiceMenu, ingredientList));
                //Menu 객체를 생성 Menu(choiceMenu, scoreing());
                //재료 객체 초기화(다음 음식을 받을 준비)
                ingredientList=new int[16];
                //progress bar 진행률 업데이트
                for(i=0; i<progFood.getMax(); i++)
                {
                    progFood.setProgress(i+1);
                    //텀을 줄 수 있는 공간 필요
                }
                Toast.makeText(getApplicationContext(), choiceMenu+"의 조리가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                txtGredient.setText("");
                txtMenu.setText("\n\n\n");
            }
        });

        //초기화 버튼을 눌렀을 경우
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메뉴 객체를 어떻게 할지 정해야 함 //굳이 어떻게 할 필요 없나? 새로운 객체를 생성하면 되서
                txtGredient.setText("");
                txtMenu.setText("\n\n\n");
                progFood.setProgress(0);
                choiceMenu = "";//굳이 필요없음
                ingredientList = new int[16];
            }
        });


    }

    //레시피 버튼을 눌렀을 때
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mInflater = getMenuInflater();
        if(v==btnRecipe){
            menu.setHeaderTitle("음식 레시피");
            mInflater.inflate(R.menu.recipe1, menu);
        }
    }

    //레시피 버튼의 세부항목을 눌렀을 때 대화상자 나오게 하기
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);   //대화상자를 위한것?
        choiceMenu = item.getTitle().toString();
        dlg.setTitle(choiceMenu + "의 레시피입니다");
        //대화상자의 '확인'버튼을 눌렀을 때
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtMenu.setText(choiceMenu+'\n');
                progFood.setProgress(0);
                Toast.makeText(getApplicationContext(), choiceMenu+"을 선택하였습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.setNegativeButton("취소", null);
        switch(item.getItemId()){
            case R.id.menu1:
                dlg.setMessage("빵/계란/햄/양상추/치즈/피클");
                dlg.show();
                return true;
            case R.id.menu2:
                dlg.setMessage("빵/계란/베이컨/양상추/치즈/피클");
                dlg.show();
                return true;
            case R.id.menu3:
                dlg.setMessage("빵/계란/해시브라운/치즈/치즈소스");
                dlg.show();
                return true;
            case R.id.menu4:
                dlg.setMessage("빵/계란/소세지/양상추/치즈");
                dlg.show();
                return true;
            case R.id.menu5:
                dlg.setMessage("빵/계란/피클/새우/양상추/치즈");
                dlg.show();
                return true;
            case R.id.menu6:
                dlg.setMessage("빵/계란/피클/불고기/양상추/치즈");
                dlg.show();
                return true;
            case R.id.menu7:
                dlg.setMessage("베이글/계란/베이컨/치즈");
                dlg.show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    //Menu 객체
    public class Menu
    {
        public String menuName;
        public int score;

        public Menu() {}

        public Menu(String name)
        {
            this.menuName = name;
        }

        public Menu(String name, int score)
        {
            this.menuName = name;
            this.score = score;
        }

        public boolean IsEqualMenu(String menu)
        {
            return menu==this.menuName;
        }
    }

    //점수를 계산하는 메서드
    public Integer scoring(String menu, int[] ingredientList)
    {
        int score=0;
        int[] recipe = recipebook.get(menu);
        for(i=0; i<ingredientList.length; i++){
            if(recipe[i]<ingredientList[i]) score+=10;
        }
        return score;
    }
}