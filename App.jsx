import { useState } from 'react'
import './App.css'
import { Box, CircularProgress, Container, FormControl, InputLabel, MenuItem, TextField, Typography } from '@mui/material';

function App() {
  
  const [chatContent, setchatContent] = useState('');
  const [tone, setTone] = useState('');
  const [generatedReply, setGeneratedReply] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit =async () => {
    setLoading(true);
    setError('');
    try{

      const response = await axios.post("http://localhost/8080/api/whatsapp/generate", 
        {
          chatContent,
          tone
        }
      );
      setGeneratedReply(typeof response.data === 'string' ? response.data : JSON.stringify(response.data));


    } catch (error) {
      setError('Failed to generate reply. Please try again.');
      console.error(error);
    } finally {
      setLoading(false);
    }
  }

  return (
    <>
     <Container maxWidth="md" sx={{py:4}}>
        <Typography variant='h3' component="h1" gutterBottom>
          WhatsApp Reply Generator
        </Typography>

        <Box sx={{mx: 3}}>
          <TextField
          fullWidth
          multiline
          rows={6}
          varient='outline'
          label="Original Chat Content"
          value={chatContent || ''}
          onChange={(e) => setchatContent(e.target.value)}
          sx={{mb: 2}}
          />

          <FormControl fullWidth sx={{mb: 2}}>
            <InputLabel>Tone (Optional)</InputLabel>
            <Select>
              value={tone || ''}
              label={"Tone (Optional)"}
              onChange={(e) => setTone(e.target.value)}
                <MenuItem value="">None</MenuItem>
                <MenuItem value="">Professional</MenuItem>
                <MenuItem value="">Friendly</MenuItem>
                <MenuItem value="">Sarcastic</MenuItem>
                <MenuItem value="">Casual</MenuItem>
            </Select>
          </FormControl>

          <Button
          varient='contained'
          onClick
          >
            {loading ? <CircularProgress size={24}/> : "Generate Reply"}
          </Button>
        </Box>

        {error && (
          <Typography color='error' sx={{ mb: 2 }}>
          {error}
          </Typography>
        )}

        {generatedReply && (
          <Box sx= {{ mt: 3 }}>
            <Typography varient='h6' gutterBottom>
              Generated Reply:
            </Typography>
            <TextField
              fullWidth
              multiline
              rows={6}
              varient='outlined'
              value={generatedReply || ''}
              inputProps={{readOnly: true}}
            />

            <Button
            varient='outline'
            sx={{mt:2}}
            onClick={() => navigator.clipboard.writeText(generatedReply)}
            >
              Copy to Clipboard
            </Button>
          </Box>
        )}

     </Container>
    </>
  )
}

export default App
