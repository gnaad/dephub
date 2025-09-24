import React from "react";
import Avatar from "@material-ui/core/Avatar";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Chip from "@material-ui/core/Chip";
import GitHubIcon from "@material-ui/icons/GitHub";
import IconButton from "@material-ui/core/IconButton";
import InfoIcon from "@material-ui/icons/Info";
import OverviewDialog from "../../utils/OverviewDialog/OverviewDialog";
import QRCodeDialog from "../../utils/QRCodeDialog/QRCodeDialog";
import ShareIcon from "@material-ui/icons/Share";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import YouTubeIcon from "@material-ui/icons/YouTube";
import { IoQrCodeOutline } from "react-icons/io5";
import { makeStyles } from "@material-ui/core";
import {
  DEPENDENCY_TITLE,
  DEPENDENCY_TEXT,
  DEVELOPED_BY,
  GITHUB_API_URL,
} from "../../constant/ApplicationConstant";
import ShareMenuDialog from "../../utils/ShareMenuDialog/ShareMenuDialog";
import "./SingleCard.css";

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

export default function Dependency({ data }) {
  const classes = useStyles();
  const [share, setShare] = React.useState(false);
  const [qrcode, setQrcode] = React.useState(false);
  const [showOverview, setShowOverview] = React.useState(false);
  const [overview, setOverview] = React.useState({
    owner: {
      type: "",
    },
  });

  const onQrCodeOpen = () => {
    setQrcode(!qrcode);
  };

  const onQrCodeClose = () => {
    setQrcode(!qrcode);
  };

  const onOverviewOpen = (data) => {
    fetch(`${GITHUB_API_URL}/${data.full_name}`)
      .then((result) => result.json())
      .then((response) => setOverview(response));
    setShowOverview(!showOverview);
  };

  const onOverviewClose = () => {
    setShowOverview(!showOverview);
  };

  const shareData = {
    title: DEPENDENCY_TITLE(data),
    text: DEPENDENCY_TEXT(data),
  };

  const onShareClick = async () => {
    if (navigator.share) {
      await navigator.share(shareData);
    } else {
      setShare(!share);
    }
  };

  const onShareMenuClose = () => {
    setShare(!share);
  };

  const onGithubClick = (data) => {
    window.open(data.github_link, "_self");
  };

  const onYoutubeClick = (data) => {
    window.open(data.youtube_link, "_self");
  };

  return (
    <>
      <Card variant="outlined" className={classes.card}>
        <CardContent className={classes.bottom}>
          <Typography variant="h6">{data.dependency_name}</Typography>

          <Typography variant="subtitle1" color="textSecondary">
            {DEVELOPED_BY(data)}
          </Typography>

          <Chip
            size="small"
            color="primary"
            variant="outlined"
            label={data.license}
          />

          {data.youtube_link !== "no" ? (
            <Chip
              size="small"
              color="primary"
              variant="outlined"
              label="YouTube"
              avatar={
                <Avatar style={{ backgroundColor: "red" }}>
                  <YouTubeIcon fontSize="inherit" className="youtube-icon" />
                </Avatar>
              }
              className="chip"
              onClick={() => onYoutubeClick(data)}
            />
          ) : null}
        </CardContent>

        <CardActions disableSpacing className={classes.top}>
          <Tooltip title="Open in GitHub">
            <IconButton onClick={() => onGithubClick(data)}>
              <GitHubIcon className="black" />
            </IconButton>
          </Tooltip>

          <Tooltip title="Share Dependency">
            <IconButton
              onClick={() => onShareClick()}
              className="dialog-content-text"
            >
              <ShareIcon className="black" />
            </IconButton>
          </Tooltip>

          <Tooltip title="Show QR Code">
            <IconButton
              onClick={() => onQrCodeOpen(data)}
              className="dialog-content-text"
            >
              <IoQrCodeOutline className="black" />
            </IconButton>
          </Tooltip>

          <Tooltip title={`Overview of ${data.dependency_name}`}>
            <IconButton
              onClick={() => onOverviewOpen(data)}
              className="dialog-content-text"
            >
              <InfoIcon className="black" />
            </IconButton>
          </Tooltip>
        </CardActions>
      </Card>

      <QRCodeDialog
        data={data}
        openQrcode={qrcode}
        closeQrCode={onQrCodeClose}
      />

      <OverviewDialog
        data={data}
        overview={overview}
        showOverview={showOverview}
        closeOverview={onOverviewClose}
      />

      <ShareMenuDialog
        data={data}
        openShare={share}
        closeShare={onShareMenuClose}
      />
    </>
  );
}
