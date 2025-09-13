package com.example.gproject

import android.os.Bundle
import android.widget.TextView // TextView를 사용하기 위해 import
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Toolbar를 사용하기 위해 import
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- 툴바 설정 시작 ---

        // 1. XML에 추가한 Toolbar를 가져와서 액션바로 설정합니다.
        // View Binding을 사용하므로 binding.toolbar로 직접 접근할 수 있습니다.
        setSupportActionBar(binding.toolbar)

        // 2. 기본으로 제공되는 제목(Title) 영역을 사용하지 않도록 설정합니다.
        // 우리는 중앙 정렬을 위해 직접 추가한 TextView를 사용할 것이기 때문입니다.
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // --- 툴바 설정 끝 ---


        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // 사용하시는 ID로 수정했습니다.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_fridge, R.id.navigation_add, R.id.navigation_recipe
            )
        )

        // 3. 내비게이션과 우리가 설정한 액션바(Toolbar)를 연동합니다.
        // 이 코드는 그대로 유지하면 됩니다.
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // 4. (가장 중요) 탭(Destination)이 변경될 때마다 우리가 만든 TextView의 텍스트를
        // 해당 탭의 제목(label)으로 변경해주는 리스너를 추가합니다.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbarTitle.text = destination.label
        }
    }
}
