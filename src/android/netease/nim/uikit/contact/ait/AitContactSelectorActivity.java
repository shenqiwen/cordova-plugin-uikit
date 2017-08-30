package com.netease.nim.uikit.contact.ait;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netease.nim.uikit.NimUIKit;
import com.rsc.calculation.R;
import com.netease.nim.uikit.cache.RobotInfoCache;
import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.recyclerview.listener.OnItemClickListener;
import com.netease.nim.uikit.contact.ait.adapter.AitContactAdapter;
import com.netease.nim.uikit.contact.ait.model.AitContactItem;
import com.netease.nim.uikit.contact.ait.model.AitContactType;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.robot.model.NimRobotInfo;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzchenkang on 2017/6/21.
 */

public class AitContactSelectorActivity extends UI {
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_ROBOT = "EXTRA_ROBOT";

    private AitContactAdapter adapter;

    private String teamId;

    private boolean addRobot;

    private List<AitContactItem> items;

    public static void start(Context context, String tid, boolean addRobot) {
        Intent intent = new Intent();
        if (tid != null) {
            intent.putExtra(EXTRA_ID, tid);
        }
        if (addRobot) {
            intent.putExtra(EXTRA_ROBOT, true);
        }
        intent.setClass(context, AitContactSelectorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_team_member_list_layout);
        parseIntent();
        initViews();
        initData();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter(recyclerView);
        ToolBarOptions options = new ToolBarOptions();
        options.titleString = "选择提醒的人";
        setToolBar(R.id.toolbar, options);
    }

    private void initAdapter(RecyclerView recyclerView) {
        items = new ArrayList<>();
        adapter = new AitContactAdapter(recyclerView, items);
        recyclerView.setAdapter(adapter);

        List<Integer> noDividerViewTypes = new ArrayList<>(1);
        noDividerViewTypes.add(AitContactType.SIMPLE_LABEL);
        recyclerView.addItemDecoration(new AitContactDecoration(this, LinearLayoutManager.VERTICAL, noDividerViewTypes));

        recyclerView.addOnItemTouchListener(new OnItemClickListener<AitContactAdapter>() {

            @Override
            public void onItemClick(AitContactAdapter adapter, View view, int position) {
                AitContactItem item = adapter.getItem(position);
                switch (item.getViewType()) {
                    case AitContactType.TEAM_MEMBER:
                        TeamMember member = (TeamMember) item.getModel();
                        AitedContacts.getInstance().aitTeamMember(member);
                        finish();
                        break;
                    case AitContactType.ROBOT:
                        NimRobotInfo robotInfo = (NimRobotInfo) item.getModel();
                        AitedContacts.getInstance().aitRobot(robotInfo);
                        finish();
                        break;
                }
            }
        });
    }

    private void parseIntent() {
        Intent intent = getIntent();
        teamId = intent.getStringExtra(EXTRA_ID);
        addRobot = intent.getBooleanExtra(EXTRA_ROBOT, false);
    }

    private void initData() {
        items = new ArrayList<AitContactItem>();
        if (addRobot) {
            initRobot();
        }
        if (teamId != null) {
            initTeamMemberAsync();
        } else {
            //data 加载结束，通知更新
            adapter.setNewData(items);
        }
    }

    private void initRobot() {
        List<NimRobotInfo> robots = RobotInfoCache.getInstance().getAllRobotAccounts();
        if (robots != null && !robots.isEmpty()) {
            items.add(0, new AitContactItem(AitContactType.SIMPLE_LABEL, "机器人"));
            for (NimRobotInfo robot : robots) {
                items.add(new AitContactItem(AitContactType.ROBOT, robot));
            }
        }
    }

    private void initTeamMemberAsync() {
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateTeamMember(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        // 继续加载群成员
                        updateTeamMember(result);
                    } else {
                        //data 加载结束，通知更新
                        adapter.setNewData(items);
                    }
                }
            });
        }
    }

    private void updateTeamMember(Team team) {
        TeamDataCache.getInstance().fetchTeamMemberList(teamId, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members) {
                if (success && members != null && !members.isEmpty()) {
                    // filter self
                    for (TeamMember member : members) {
                        if (member.getAccount().equals(NimUIKit.getAccount())) {
                            members.remove(member);
                            break;
                        }
                    }

                    if (!members.isEmpty()) {
                        items.add(new AitContactItem(AitContactType.SIMPLE_LABEL, "群成员"));
                        for (TeamMember member : members) {
                            items.add(new AitContactItem(AitContactType.TEAM_MEMBER, member));
                        }
                    }
                }
                //data 加载结束，通知更新
                adapter.setNewData(items);
            }
        });
    }
}