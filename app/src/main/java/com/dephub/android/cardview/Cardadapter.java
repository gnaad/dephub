package com.dephub.android.cardview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dephub.android.R;
import com.dephub.android.activity.Web;

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
                startWebView(model);
            }
        });

        holder.dependencyNameAdapter.setOnLongClickListener(new View.OnLongClickListener() {
                                                                @Override
                                                                public boolean onLongClick(View v) {
                                                                    progressDialog.show();

                                                                    String fullName = model.getFullName();
                                                                    String dependencyName = model.getDependencyName();

                                                                    RequestQueue queue = Volley.newRequestQueue(view.getContext());

                                                                    String url = "https://api.github.com/repos/" + fullName;

                                                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
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

                                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                                                                                alertDialogBuilder.setCancelable(true);
                                                                                alertDialogBuilder.setMessage("Overview of " + dependencyName +
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
                                                                                        "\n\nLicense Name : " + licenseName);
                                                                                alertDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.dismiss();
                                                                                        context.getCacheDir().delete();
                                                                                    }
                                                                                });
                                                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                                                alertDialog.show();
                                                                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(view.getResources().getColor(R.color.colorAccent));
                                                                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(view.getResources().getColor(R.color.colorAccent));
                                                                            } catch (JSONException e) {
                                                                                progressDialog.dismiss();
                                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.CustomAlertDialog);

                                                                                alertDialogBuilder.setCancelable(true);
                                                                                alertDialogBuilder.setMessage("Oops...\n\nDependency Id : " + id + "\n\nWe've got some bad news.\n\nThere was problem while loading overview of " + dependencyName + " dependency.\n\nWould you like to open dependency?");
                                                                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        startWebView(model);
                                                                                    }
                                                                                });
                                                                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                                                alertDialog.show();
                                                                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.colorAccent));
                                                                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.colorAccent));

                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(context, "Failed to load overview with code " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                    queue.add(jsonObjectRequest);
                                                                    return false;
                                                                }
                                                            }
        );
        holder.dependencyDeveloperAdapter.setText(model.getDeveloperName());

        holder.githubLinkAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String link = model.getGithubLink();
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(28);
                ClipboardManager clipboard1 = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip1 = ClipData.newPlainText("DepHub", link);
                clipboard1.setPrimaryClip(clip1);
                Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        holder.githubLinkAdapter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                startWebView(model);
            }
        });
        holder.shareAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = model.getDependencyName();
                String link = model.getGithubLink();
                String id = model.getId();
                Intent intent60 = new Intent(Intent.ACTION_SEND);
                intent60.setType("text/plain");
                String shareBody10 = "About Android Dependency";
                String shareSub10 = "Hi there\n\nDependency Id : " + id + "\nDependency Name : " + name + "\nDependency Link : " + link + "\n\nIn-App link : https://dephub.co/app/" + id + "\n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                intent60.putExtra(Intent.EXTRA_SUBJECT, shareBody10);
                intent60.putExtra(Intent.EXTRA_TEXT, shareSub10);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    intent60.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                view.getContext().startActivity(Intent.createChooser(intent60, "Share this Dependency using"));
            }
        });
        holder.shareAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = model.getDependencyName();
                String link = model.getGithubLink();
                String id = model.getId();
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(28);
                ClipboardManager clipboard1 = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip1 = ClipData.newPlainText("DepHub", "Hi there\n\nDependency Id : " + id + "\nDependency Name : " + name + "\nDependency Link : " + link + "\n\nIn-App link : https://dephub.co/app/" + id + "\n\nInformation Delivered by : DepHub\nInformation Provided by : Github\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow");
                clipboard1.setPrimaryClip(clip1);
                Toast.makeText(context, "All details copied", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void startWebView(CardModel model) {
        String githubLink = model.getGithubLink();
        String youtubeLink = model.getYoutubeLink();
        String dependencyName = model.getDependencyName();
        String developerName = model.getDeveloperName();
        String fullName = model.getFullName();
        String id = model.getId();
        String license = model.getLicense();
        String licenseLink = model.getLicenseLink();

        Intent intent = new Intent(context, Web.class);
        intent.putExtra("githubLink", githubLink);
        intent.putExtra("dependencyName", dependencyName);
        intent.putExtra("youtubeLink", youtubeLink);
        intent.putExtra("developerName", developerName);
        intent.putExtra("id", id);
        intent.putExtra("license", license);
        intent.putExtra("licenseLink", licenseLink);
        intent.putExtra("fullName", fullName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
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
