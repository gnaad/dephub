import { makeStyles } from "@material-ui/core";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Chip from "@material-ui/core/Chip";
import CircularProgress from "@material-ui/core/CircularProgress";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import IconButton from "@material-ui/core/IconButton";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import GitHubIcon from "@material-ui/icons/GitHub";
import InfoIcon from "@material-ui/icons/Info";
import ShareIcon from "@material-ui/icons/Share";
import YouTubeIcon from "@material-ui/icons/YouTube";
import React from "react";
import { IoQrCodeOutline } from "react-icons/io5";
import QRCode from "react-qr-code";

const useStyles = makeStyles({
  card: {
    marginTop: "0%",
  },
  bottom: {
    paddingBottom: "0%",
  },
  top: {
    paddingTop: "0%",
  },
});

export default function DependencyCard({ data }) {
  const classes = useStyles();
  const [share, setShare] = React.useState(false);
  const [qrcode, setQrcode] = React.useState(false);
  const [showOverview, setshowOverview] = React.useState(false);
  const [overview, setOverview] = React.useState({
    owner: {
      type: "",
    },
  });

  const closeShare = () => {
    setShare(false);
  };

  const closeQRCode = () => {
    setQrcode(false);
  };

  const closeOverview = () => {
    setshowOverview(false);
  };

  const showQR = () => {
    setQrcode(true);
  };

  const openOverview = (data) => {
    fetch(`https://api.github.com/repos/${data.full_name}`)
      .then((result) => result.json())
      .then((response) => setOverview(response));

    setshowOverview(true);
  };

  const sharedata = {
    title: `${data.dependency_name} Dependency`,
    text: `Hi there\n\nDependency Id : ${data.id}\nDependency Name : ${data.dependency_name}\nDependency Link : ${data.github_link}
    \n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub
    \nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow
    `,
  };

  const githubClick = () => {
    window.open(data.github_link, "_self");
  };

  const openYoutube = () => {
    window.open(data.youtube_link, "_self");
  };

  const shareClick = async () => {
    if (navigator.share) {
      await navigator.share(sharedata);
    } else {
      setShare(true);
    }
  };

  return (
    <div>
      <Card variant="outlined" className={classes.card}>
        <CardContent className={classes.bottom}>
          <Typography variant="h6">{data.dependency_name}</Typography>
          <Typography variant="subtitle1" color="textSecondary">
            {`Developed By : ${data.developer_name}`}
          </Typography>
          <Chip
            color="primary"
            variant="outlined"
            size="small"
            label={data.license}
          />
          {!(data.youtube_link === "no") ? (
            <Chip
              variant="outlined"
              size="small"
              color="primary"
              label="YouTube"
              avatar={
                <Avatar style={{ backgroundColor: "red" }}>
                  <YouTubeIcon fontSize="inherit" style={{ color: "white" }} />
                </Avatar>
              }
              style={{ marginLeft: "1.5%" }}
              onClick={openYoutube}
            />
          ) : null}
        </CardContent>
        <CardActions disableSpacing className={classes.top}>
          <Tooltip title="Open in GitHub">
            <IconButton
              aria-label="Open in GitHub"
              onClick={() => githubClick()}
            >
              <GitHubIcon style={{ color: "black" }} />
            </IconButton>
          </Tooltip>

          <Tooltip title="Share Dependency">
            <IconButton
              aria-label="Share Dependency"
              onClick={() => shareClick()}
              style={{ whiteSpace: "break-spaces" }}
            >
              <ShareIcon style={{ color: "black" }} label="Share" />
            </IconButton>
          </Tooltip>

          <Tooltip title="Show QR Code">
            <IconButton
              aria-label="Show QR Code"
              onClick={() => showQR()}
              style={{ whiteSpace: "break-spaces" }}
            >
              <IoQrCodeOutline style={{ color: "black" }} label="Share" />
            </IconButton>
          </Tooltip>

          <Tooltip title={`Overview of ${data.dependency_name}`}>
            <IconButton
              aria-label="Show QR Code"
              onClick={() => openOverview(data)}
              style={{ whiteSpace: "break-spaces" }}
            >
              <InfoIcon style={{ color: "black" }} label="Share" />
            </IconButton>
          </Tooltip>
        </CardActions>
      </Card>

      {/* Share Menu */}
      <Dialog
        open={share}
        onClose={closeShare}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        fullWidth
      >
        <DialogTitle id="alert-dialog-title">Share Dependency</DialogTitle>
        <DialogContent>
          <DialogContentText
            id="alert-dialog-description"
            style={{ whiteSpace: "break-spaces" }}
          >
            {`Hi there\n\nDependency Id : ${data.id}\nDependency Name : ${data.dependency_name}\nDependency Link : ${data.github_link}
            \n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub
            \nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow
            `}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeShare} color="primary" autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>

      {/* QR Code */}
      <Dialog
        open={qrcode}
        onClose={closeQRCode}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          QR Code of {data.dependency_name}
        </DialogTitle>
        <DialogContent>
          <DialogContentText
            id="alert-dialog-description"
            style={{ whiteSpace: "break-spaces" }}
          >
            {`Dependency Name : ${data.dependency_name}\nDeveloper Name : ${data.developer_name}`}
          </DialogContentText>
          <DialogContent>
            <QRCode
              style={{ display: "block", margin: "0 auto" }}
              size={200}
              value={`${data.github_link}`}
            />
          </DialogContent>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeQRCode} color="primary" autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>

      {/* Overview */}
      <Dialog
        open={showOverview}
        onClose={closeOverview}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        fullWidth
      >
        <DialogTitle id="alert-dialog-title">
          Overview of {data.dependency_name}
        </DialogTitle>
        {overview.owner.type === "" ? (
          <DialogContent>
            <CircularProgress style={{ display: "block", margin: "0 auto" }} />
          </DialogContent>
        ) : (
          <DialogContent>
            <DialogContentText
              id="alert-dialog-description"
              style={{ whiteSpace: "break-spaces" }}
            >
              {`Dependency Id : ${data.id}\n\nName : ${data.dependency_name}\nDeveoper : ${data.developer_name}\nType : ${overview.owner.type}
            \nFork : ${overview.forks}\nStar : ${overview.watchers_count}\nWatch : ${overview.subscribers_count}
            \nOpen Issue Count : ${overview.open_issues_count}\nLanguage : ${overview.language}
            \nDescription : ${overview.description}
            \nLicense : ${data.license}`}
            </DialogContentText>
          </DialogContent>
        )}
        <DialogActions>
          <Button onClick={closeOverview} color="primary" autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
