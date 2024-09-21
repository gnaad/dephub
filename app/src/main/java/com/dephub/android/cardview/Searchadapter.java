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
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Dependency> cardArrayList;

    ProgressDialog progressDialog;
    String id;
    View view;

    public SearchAdapter(ArrayList<Dependency> cardArrayList, Context context) {
        this.context = context;
        this.cardArrayList = cardArrayList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Dependency> filterList) {
        cardArrayList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.defaultcardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        progressDialog = new ProgressDialog(context, R.style.customAlertDialog);
        progressDialog.setMessage(ApplicationConstant.GETTING_OVERVIEW);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        Dependency model = cardArrayList.get(position);
        holder.dependencyNameAdapter.setText(model.getDependencyName());
        holder.dependencyNameAdapter.setOnClickListener(v -> Snippet.openWeb(context, model));
        holder.dependencyNameAdapter.setOnLongClickListener(v -> {
            progressDialog.show();
            String fullName = model.getFullName();
            String dependencyName = model.getDependencyName();
            String url = ApplicationConstant.GITHUB_API + fullName;

            Snippet.dependencyObject(context,
                    url,
                    response -> {
                        try {
                            progressDialog.dismiss();
                            id = model.getId();
                            String developer = model.getDeveloperName();
                            String name = response.getString(ApplicationConstant.GH_NAME);

                            JSONObject license = response.getJSONObject(ApplicationConstant.GH_LICENSE);
                            String licenseName = license.getString(ApplicationConstant.GH_NAME);

                            String description = response.getString(ApplicationConstant.GH_DESCRIPTION);
                            String language = response.getString(ApplicationConstant.GH_LANGUAGE);
                            String star = response.getString(ApplicationConstant.GH_WATCHERS_COUNT);
                            String forkCount = response.getString(ApplicationConstant.GH_FORKS_COUNT);
                            String watch = response.getString(ApplicationConstant.GH_SUBSCRIBERS_COUNT);

                            JSONObject data = response.getJSONObject(ApplicationConstant.GH_OWNER);
                            String type = data.getString(ApplicationConstant.GH_TYPE);
                            String openIssueCount = response.getString(ApplicationConstant.GH_OPEN_ISSUES_COUNT);

                            Widget.alertDialog(context,
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
                                            "\nLanguage : " + language +
                                            "\n\nDescription : " + description +
                                            "\n\nLicense Name : " + licenseName,
                                    ApplicationConstant.CLOSE,
                                    null,
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                        context.getCacheDir().delete();
                                    },
                                    null);
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Widget.alertDialog(context,
                                    true,
                                    "Oops...\n\nDependency Id : " + id + "\n\nThere was problem while loading overview of " + dependencyName + " dependency.\n\nWould you like to open dependency?",
                                    ApplicationConstant.YES,
                                    ApplicationConstant.NO,
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
                        Widget.Toast(context, "Failed to load overview. Response code is " + error.networkResponse.statusCode);
                    });
            return false;
        });
        holder.dependencyDeveloperAdapter.setText(model.getDeveloperName());
        holder.githubLinkAdapter.setOnLongClickListener(v -> {
            String link = model.getGithubLink();
            Snippet.vibrate(context, link);
            Widget.Toast(context, "Link copied");
            return false;
        });
        holder.githubLinkAdapter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Snippet.openWeb(context, model);
            }
        });
        holder.shareAdapter.setOnClickListener(v -> {
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
        });
        holder.shareAdapter.setOnLongClickListener(v -> {
            String name = model.getDependencyName();
            String link = model.getGithubLink();
            String id = model.getId();
            Snippet.vibrate(context, "Hi there\n\nDependency Id : " + id + "\nDependency Name : " + name + "\nDependency Link : " + link + "\n\nInformation Delivered by : DepHub\nInformation Provided by : Github\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow");
            Widget.Toast(context, "All details copied");
            return false;
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
            githubLinkAdapter = itemView.findViewById(R.id.github);
            shareAdapter = itemView.findViewById(R.id.share);
            dependencyNameAdapter = itemView.findViewById(R.id.dep_name);
            dependencyDeveloperAdapter = itemView.findViewById(R.id.dev_name);
        }
    }
}
