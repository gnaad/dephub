package com.dephub.android.cardview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final Context context;
    View view;
    String id;
    ProgressDialog progressDialog;
    private final ArrayList<CardModel> cardArrayList;

    public CardAdapter(ArrayList<CardModel> cardArrayList, Context context) {
        this.context = context;
        this.cardArrayList = cardArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defaultcardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        progressDialog = new ProgressDialog(context, R.style.CustomAlertDialog);
        progressDialog.setMessage("Getting Overview");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        CardModel model = cardArrayList.get(position);
        holder.dependencyNameAdapter.setText(model.getDependencyName());
        holder.dependencyNameAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snippet.openWeb(context, model);
            }
        });

        holder.dependencyNameAdapter.setOnLongClickListener(view -> {
                    progressDialog.show();
                    String fullName = model.getFullName();
                    String dependencyName = model.getDependencyName();
                    String url = "https://api.github.com/repos/" + fullName;

                    Snippet.dependencyObject(this.view.getContext(), url,
                            response -> {
                                try {
                                    progressDialog.dismiss();
                                    id = model.getId();
                                    String developer = model.getDeveloperName();
                                    String name = response.getString("name");

                                    JSONObject license = response.getJSONObject("license");
                                    String licenseName = license.getString("name");

                                    String desc = response.getString("description");
                                    String lang = response.getString("language");
                                    String star = response.getString("watchers_count");
                                    String forkCount = response.getString("forks_count");
                                    String watch = response.getString("subscribers_count");

                                    JSONObject data = response.getJSONObject("owner");
                                    String type = data.getString("type");
                                    String openIssueCount = response.getString("open_issues_count");
                                    Component.alertDialog(context,
                                            true,
                                            "Overview of " + dependencyName +
                                                    "\n\nDependency Id : " + id +
                                                    "\n\nName : " + name +
                                                    "\nDeveloper : " + developer +
                                                    "\nType : " + type +
                                                    "\n\nFork : " + forkCount +
                                                    "\nStar : " + star +
                                                    "\nWatch : " + watch +
                                                    "\n\nOpen Issue Count : " + openIssueCount +
                                                    "\nLanguage : " + lang +
                                                    "\n\nDescription : " + desc +
                                                    "\n\nLicense Name : " + licenseName,
                                            "Close",
                                            null,
                                            (dialog, which) -> {
                                                dialog.dismiss();
                                                context.getCacheDir().delete();
                                            },
                                            null);
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    Component.alertDialog(context,
                                            true,
                                            "Oops...\n\nDependency Id : " + id + "\n\nWe've got some bad news.\n\nThere was problem while loading overview of " + dependencyName + " dependency.\n\nWould you like to open dependency?",
                                            "Yes",
                                            "No",
                                            (dialog, which) -> {
                                                Snippet.openWeb(context, model);
                                            },
                                            (dialog, which) -> {
                                                dialog.dismiss();
                                            });
                                }
                            },
                            error -> {
                                progressDialog.dismiss();
                                Component.Toast(context, "Failed to load overview with code " + error.networkResponse.statusCode);
                            });
                    return false;
                }
        );
        holder.dependencyDeveloperAdapter.setText(model.getDeveloperName());

        holder.githubLinkAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String link = model.getGithubLink();
                Snippet.vibrate(context, link);
                Component.Toast(context, "Link copied");
                return false;
            }
        });
        holder.githubLinkAdapter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Snippet.openWeb(context, model);
            }
        });
        holder.shareAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = model.getDependencyName();
                String link = model.getGithubLink();
                String id = model.getId();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "About Android Dependency";
                String shareSub = "Hi there\n\nDependency Id : " + id + "\nDependency Name : " + name + "\nDependency Link : " + link + "\n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                intent.putExtra(Intent.EXTRA_TEXT, shareSub);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                view.getContext().startActivity(Intent.createChooser(intent, "Share this Dependency using"));
            }
        });
        holder.shareAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = model.getDependencyName();
                String link = model.getGithubLink();
                String id = model.getId();
                Snippet.vibrate(context, "Hi there\n\nDependency Id : " + id + "\nDependency Name : " + name + "\nDependency Link : " + link + "\n\nInformation Delivered by : DepHub\nInformation Provided by : Github\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow");
                Component.Toast(context, "All details copied");
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton githubLinkAdapter;
        private final ImageButton shareAdapter;
        private final TextView dependencyNameAdapter;
        private final TextView dependencyDeveloperAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView cardBackgroundAdapter = itemView.findViewById(R.id.cardview);
            githubLinkAdapter = itemView.findViewById(R.id.github);
            shareAdapter = itemView.findViewById(R.id.share);
            dependencyNameAdapter = itemView.findViewById(R.id.depname);
            dependencyDeveloperAdapter = itemView.findViewById(R.id.devname);
        }
    }
}