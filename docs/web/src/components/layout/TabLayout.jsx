import { useState } from "react";
import AppBar from "./Appbar/Appbar";
import SingleTab from "./SingleTab";
import Tab from "@material-ui/core/Tab";
import Tabs from "@material-ui/core/Tabs";
import TabPanel from "./TabPanel";

export default function TabLayout() {
  const [value, setValue] = useState(0);

  const props = (index) => {
    return {
      id: `scrollable-prevent-tab-${index}`,
      "aria-controls": `scrollable-prevent-tabpanel-${index}`,
    };
  };

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <>
      <AppBar />
      <Tabs
        value={value}
        onChange={handleChange}
        indicatorColor="primary"
        variant="scrollable"
        scrollButtons="auto"
      >
        <Tab label="Text" {...props(0)} />
        <Tab label="Button" {...props(1)} />
        <Tab label="Widget" {...props(2)} />
        <Tab label="Layout" {...props(3)} />
        <Tab label="Container" {...props(4)} />
        <Tab label="Helper" {...props(5)} />
        <Tab label="Google" {...props(6)} />
        <Tab label="Legacy" {...props(7)} />
        <Tab label="Others" {...props(8)} />
      </Tabs>

      <TabPanel value={value} index={0}>
        <SingleTab dependencyType="Text" />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <SingleTab dependencyType="Button" />
      </TabPanel>
      <TabPanel value={value} index={2}>
        <SingleTab dependencyType="Widget" />
      </TabPanel>
      <TabPanel value={value} index={3}>
        <SingleTab dependencyType="Layout" />
      </TabPanel>
      <TabPanel value={value} index={4}>
        <SingleTab dependencyType="Container" />
      </TabPanel>
      <TabPanel value={value} index={5}>
        <SingleTab dependencyType="Helper" />
      </TabPanel>
      <TabPanel value={value} index={6}>
        <SingleTab dependencyType="Google" />
      </TabPanel>
      <TabPanel value={value} index={7}>
        <SingleTab dependencyType="Legacy" />
      </TabPanel>
      <TabPanel value={value} index={8}>
        <SingleTab dependencyType="Others" />
      </TabPanel>
    </>
  );
}
