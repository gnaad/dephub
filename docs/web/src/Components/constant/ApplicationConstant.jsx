export const API_URL = "https://gnaad.github.io/dephub/json/dependency.json";
export const GITHUB_API_URL = "https://api.github.com/repos";
export const EMAIL_ADDRESS = "mailto:mailtodephub@gmail.com";
export const PLAYSTORE_URL ="https://play.google.com/store/apps/details?id=com.dephub.android";
export const FEEDBACK_URL = "https://gnaad.github.io/dephub/feedback.html";

export const QR_CODE_TITLE = (data) => {
  return `QR Code of ${data.dependency_name}`;
};

export const QR_CODE_DESCRIPTION = (data) => {
  return `Dependency Name : ${data.dependency_name}
Developer Name : ${data.developer_name}`;
};

export const OVERVIEW_TITLE = (data) => {
  return `Overview of ${data.dependency_name}`;
};

export const OVERVIEW_DESCRIPTION = (data,overview) => {
  return `Dependency Id : ${data.id}

Name : ${data.dependency_name}
Deveoper : ${data.developer_name}
Type : ${overview.owner.type}
Fork : ${overview.forks}
Star : ${overview.watchers_count}
Watch : ${overview.subscribers_count}
Open Issue Count : ${overview.open_issues_count}
Language : ${overview.language}
Description : ${overview.description}
License : ${data.license}`;
};

export const DEVELOPED_BY = (data) => {
  return `Developed By : ${data.developer_name}`;
};

export const DEPENDENCY_TITLE = (data) => {
  return `${data.dependency_name} Dependency`;
};

export const DEPENDENCY_TEXT = (data) => {
  return `Hi there

Dependency Id : ${data.id}
Dependency Name : ${data.dependency_name}
Dependency Link : ${data.github_link}

Information Delivered by : DepHub
Information Provided by : GitHub

Download our Android App : https://bit.ly/installdephubapp

Thank You
Let's code for a better tomorrow`;
};
