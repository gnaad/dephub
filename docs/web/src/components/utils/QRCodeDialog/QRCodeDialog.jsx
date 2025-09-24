import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import QRCode from "react-qr-code";
import {
  QR_CODE_DESCRIPTION,
  QR_CODE_TITLE,
} from "../../constant/ApplicationConstant";
import "./QRCodeDialog.css";

export default function QRCodeDialog({ data, openQrcode, closeQrCode }) {
  return (
    <>
      <Dialog open={openQrcode} onClose={closeQrCode}>
        <DialogTitle>{QR_CODE_TITLE(data)}</DialogTitle>

        <DialogContent>
          <DialogContentText className="dialog-content-text">
            {QR_CODE_DESCRIPTION(data)}
          </DialogContentText>

          <DialogContent>
            <QRCode
              className="dialog-content"
              size={200}
              value={`${data.github_link}`}
            />
          </DialogContent>
        </DialogContent>

        <DialogActions>
          <Button onClick={closeQrCode} color="primary" autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
