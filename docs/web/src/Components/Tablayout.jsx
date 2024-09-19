import { Tooltip } from "@material-ui/core";
import AppBar from "@material-ui/core/AppBar";
import Box from "@material-ui/core/Box";
import IconButton from "@material-ui/core/IconButton";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import { makeStyles } from "@material-ui/core/styles";
import Tab from "@material-ui/core/Tab";
import Tabs from "@material-ui/core/Tabs";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import PropTypes from "prop-types";
import React, { useState } from "react";
import Package from "../../package.json";
import Common from "../Tabs/Common";

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`scrollable-prevent-tabpanel-${index}`}
      aria-labelledby={`scrollable-prevent-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired,
};

function a11yProps(index) {
  return {
    id: `scrollable-prevent-tab-${index}`,
    "aria-controls": `scrollable-prevent-tabpanel-${index}`,
  };
}

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    backgroundColor: theme.palette.background.paper,
  },
  tab: {
    backgroundColor: "white",
    color: "black",
    justifyContent: "center",
  },
  scroller: {
    flexGrow: "0",
  },
}));

export default function Tablayout() {
  const [value, setValue] = useState(0);
  const [options, setOptions] = useState(null);
  const classes = useStyles();

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const openOptions = (event) => {
    setOptions(event.currentTarget);
  };

  const closeOptions = () => {
    setOptions(null);
  };

  const openMail = () => {
    window.open("mailto:mailtodephub@gmail.com");
  };

  const openApp = () => {
    window.open(
      "https://play.google.com/store/apps/details?id=com.dephub.android",
      "_self"
    );
  };

  const openFeedback = () => {
    window.open("https://gnaad.github.io/dephub/feedback", "_self");
  };

  return (
    <div className={classes.root}>
      <AppBar position="sticky" elevation={0}>
        <Toolbar>
          <Typography variant="h6">
            DepHub Web <small style={{ fontSize: "10px" }}>Beta</small>
          </Typography>
          <Tooltip title="More Options">
            <IconButton
              aria-label="More Options"
              onClick={(event) => openOptions(event)}
              style={{
                marginLeft: "auto",
                marginRight: "0%",
              }}
            >
              <MoreVertIcon style={{ color: "white" }} label="More Options" />
            </IconButton>
          </Tooltip>
          <Menu
            id="simple-menu"
            anchorEl={options}
            keepMounted
            open={Boolean(options)}
            onClose={closeOptions}
          >
            <MenuItem onClick={() => openMail()}>Mail Us</MenuItem>
            <MenuItem onClick={() => openApp()}>Download Official App</MenuItem>
            <MenuItem onClick={() => openFeedback()}>Write Feedback</MenuItem>
            <MenuItem>Version - {Package.version}</MenuItem>
          </Menu>
        </Toolbar>
        <Tabs
          value={value}
          onChange={handleChange}
          indicatorColor="primary"
          variant="scrollable"
          scrollButtons="auto"
          classes={{ root: classes.tab, scroller: classes.scroller }}
          aria-label="scrollable prevent tabs example"
        >
          <Tab label="Text" aria-label="Text" {...a11yProps(0)} />
          <Tab label="Button" aria-label="Button" {...a11yProps(1)} />
          <Tab label="Widget" aria-label="Widget" {...a11yProps(2)} />
          <Tab label="Layout" aria-label="Layout" {...a11yProps(3)} />
          <Tab label="Container" aria-label="Container" {...a11yProps(4)} />
          <Tab label="Helper" aria-label="Helper" {...a11yProps(5)} />
          <Tab label="Google" aria-label="Google" {...a11yProps(6)} />
          <Tab label="Legacy" aria-label="Legacy" {...a11yProps(7)} />
          <Tab label="Others" aria-label="Others" {...a11yProps(8)} />
        </Tabs>
      </AppBar>
      <TabPanel value={value} index={0}>
        <Common dependencyType="Text" />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <Common dependencyType="Button" />
      </TabPanel>
      <TabPanel value={value} index={2}>
        <Common dependencyType="Widget" />
      </TabPanel>
      <TabPanel value={value} index={3}>
        <Common dependencyType="Layout" />
      </TabPanel>
      <TabPanel value={value} index={4}>
        <Common dependencyType="Container" />
      </TabPanel>
      <TabPanel value={value} index={5}>
        <Common dependencyType="Helper" />
      </TabPanel>
      <TabPanel value={value} index={6}>
        <Common dependencyType="Google" />
      </TabPanel>
      <TabPanel value={value} index={7}>
        <Common dependencyType="Legacy" />
      </TabPanel>
      <TabPanel value={value} index={8}>
        <Common dependencyType="Others" />
      </TabPanel>
    </div>
  );
}
